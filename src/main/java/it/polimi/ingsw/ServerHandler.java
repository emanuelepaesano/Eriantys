package it.polimi.ingsw;

import it.polimi.ingsw.ViewStateArgs.ViewStateArgument;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerHandler {
    int port;
    String model;
    Scanner inStream;
    ObjectOutputStream outStream;
    ServerSocket serverSocket;
    List<VirtualView> views;
    //To the first client we ask the number of players, which is the first thing inside the gamecontroller
    public ServerHandler(int port) {
        this.port = port;
        views = new ArrayList<>();
    }

    // TODO: 29/04/2022 obviously this will need to handle more than 1 client
    public int startServer() throws IOException{

        try {serverSocket = new ServerSocket(port);}
        catch (IOException e) {System.err.println(e.getMessage()); return 0;}
        System.out.println("Server ready");

        Socket socket = serverSocket.accept();
        System.out.println("Connected!");
        VirtualView client1 = new VirtualView(socket,1);
        views.add(client1);
        client1.update("Welcome! How many players?");
        int n = Integer.parseInt(client1.getAnswer());
        client1.update("OK! Waiting players for a "+n+"-player game...");
        lookForMorePlayers((n-1));
        System.out.println(views);
        return n;
    }
    public void lookForMorePlayers(int n) throws IOException {
        for (int i=0; i<n ; i++){
            Socket socket = serverSocket.accept();
            VirtualView clientView = new VirtualView(socket,i+2);
            views.add(clientView);
            clientView.update("");
            System.out.println("Connected! We are at " + (i+2) + " players out of " + (n+1));
        }
        updateAllViews("We can start the game! ("+ (n+1) +"-player game)");
    }


    public void updateView(Player player, ViewStateArgument message) throws IOException {
        int playerId = player.getId();
        if (views.get(playerId-1).getPlayerId() == playerId) {
            views.get(playerId - 1).update(message);
        }
        else {
            System.err.println("Something went very wrong");
        }
    }
    public void updateAllViews(Object message) {
        for(VirtualView client: views){
            client.update(message);
        }
    }




}

