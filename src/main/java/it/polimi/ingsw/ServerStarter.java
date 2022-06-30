package it.polimi.ingsw;

import it.polimi.ingsw.messages.EndGameMessage;
import it.polimi.ingsw.messages.FirstClientMessage;
import it.polimi.ingsw.messages.LoginMessage;
import it.polimi.ingsw.messages.NoReplyMessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerStarter {
    int port;
    String model;
    Scanner inStream;
    ObjectOutputStream outStream;
    ServerSocket serverSocket;
    List<VirtualView> views;

    private static ServerStarter server;

    public ServerStarter(int port) {
        this.port = port;
        views = new ArrayList<>();
        server = this;
    }

    /**
     * Stop the game if client is disconnected from the server.
     *
     * @param OK true if the connection is ok.
     */
    public static void stopGame(Boolean OK) {
        if(!OK) {
            new NoReplyMessage(true,"Disconnection","Disconnection Error","Something went wrong. Either all player disconnected\n" +
                    "or someone disconnected before the game started. The game will stop.").send(server.views);
        }
        try {
            for (VirtualView view: server.views){
                view.getSocket().close();
            }
        server.serverSocket.close();
        System.out.println("all sockets closed.");
        } catch (IOException e) {e.printStackTrace();}
        System.exit(0);
    }

    /**
     * Start server.
     * This method creates new socket, makes VirtualView for the first client, and ask the number of players.
     *
     * @return the number of players
     * @throws IOException
     */
    public int startServer() throws IOException{
        try {serverSocket = new ServerSocket(port);}
        catch (IOException e) {System.err.println(e.getMessage()); return 0;}
        System.out.println("Server ready");

        Socket socket = serverSocket.accept();
        System.out.println("Connected!");
        VirtualView client1 = new VirtualView(this, socket,1);
        views.add(client1);
        int n = askForPN(client1);
        new NoReplyMessage(false,"OK","","OK! Waiting players for a "+n+"-player game...").send(client1);

        lookForMorePlayers((n-1));
        System.out.println(views);
        return n;
    }

    /**
     * Look for other players until the number of players reaches the selected number by the first client.
     *
     * @param n the number of players
     * @throws IOException
     */
    public void lookForMorePlayers(int n) throws IOException {
        for (int i=0; i<n ; i++){
            Socket socket = serverSocket.accept();
            VirtualView clientView = new VirtualView(this, socket,i+2);
            views.add(clientView);
            new LoginMessage("joining an existing game ... ("+(n+1)+"-player game)" ).send(clientView);
            System.out.println("Connected to client! We are at " + (i+2) + " players out of " + (n+1));
        }
        try{Thread.sleep(100);}catch(Exception ex){}
    }


    /**
     * Ask the number of players to the first client.
     *
     * @param client VirtualView of the client
     * @return the number of players
     */
    private int askForPN(VirtualView client)  {
        int input = 0;
        while ((input != 3) && (input != 2)) {
            new FirstClientMessage("Welcome! How many players?").send(client);
            try {
                input = Integer.parseInt(client.getReply());
            } catch (DisconnectedException ex){ stopGame(false); }
            System.out.println("received reply: "+ input);
        }
        return input;
    }

    static ActionListener declareWin = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            synchronized (ServerApp.lock){
                ServerApp.lock.notifyAll();
            }
            new EndGameMessage(List.of(), EndGameMessage.EndGameType.WIN).send(server.views);
            stopGame(true);
        }
    };

    /**
     * This method is called when player is disconnected.
     * Notify the other players that the player is disconnected.
     *
     * @param view VirtualView of the player disconnected
     */
    public void aViewDisconnected(VirtualView view) {
        List<VirtualView> activeViews = server.views.stream().filter(v->!v.isDisconnected()).toList();
        if (activeViews.size()>1) {
            new NoReplyMessage(true,"Disconnection","Player Disconnection","Player " + view.getPlayerId() + " disconnected. The game will continue without that player.\n" +
                    "Players may wait for reconnection or keep playing.").send(activeViews);
        }
        else{
            new NoReplyMessage(true,"Last Player","You are alone!","You are the only player remaining online.\n" +
                    "If no player reconnects, you will win in 45 seconds.").send(activeViews.get(0));
        }

    }

    /**
     * This method is called when the player is reconnected.
     * Notify the other players that the player is reconnected.
     *
     * @param view VirtualView of the player reconnected.
     */
    public void aViewReconnected(VirtualView view) {
        System.out.println("a view: " + view + "reconnected.");
        List<VirtualView> otherViews = new ArrayList<>(views);
        otherViews.remove(view);
        new NoReplyMessage(false,"Reconnection", "Player reconnection","Player "+ view.getPlayerId() + " is back online.\n" +
                "They will resume playing from the next Planning Phase.").send(otherViews);
        synchronized (ServerApp.lock) {
            ServerApp.lock.notifyAll();
        }
    }
}

