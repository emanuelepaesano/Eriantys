package it.polimi.ingsw;

import it.polimi.ingsw.messages.StringMessage;
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

    // TODO: 01/05/2022 Multithreading (one for every client)
    public int startServer() throws IOException{

        try {serverSocket = new ServerSocket(port);}
        catch (IOException e) {System.err.println(e.getMessage()); return 0;}
        System.out.println("Server ready");

        Socket socket = serverSocket.accept();
        System.out.println("Connected!");
        VirtualView client1 = new VirtualView(socket,1);
        views.add(client1);
        int n = askForPN(client1);
        new StringMessage("OK! Waiting players for a "+n+"-player game...").send(client1);
        lookForMorePlayers((n-1));
        System.out.println(views);
        return n;
    }
    public void lookForMorePlayers(int n) throws IOException {
        for (int i=0; i<n ; i++){
            Socket socket = serverSocket.accept();
            VirtualView clientView = new VirtualView(socket,i+2);
            views.add(clientView);
            new StringMessage("joining an existing game ... ("+(n+1)+"-player game)" ).send(clientView);
            System.out.println("Connected! We are at " + (i+2) + " players out of " + (n+1));
        }
        new StringMessage("We can start the game! ("+ (n+1) +"-player game)").send(views);
    }


    private int askForPN(VirtualView client)  {
        int input = 0;
        while ((input != 3) && (input != 2)) {
                new StringMessage("Welcome! How many players?").send(client);
                input = Integer.parseInt((client.getAnswer()).toString());
        }
        return input;
    }


    public void closeAll() throws IOException {
        inStream.close();
        outStream.close();
        views.forEach(v-> {
            try {
                v.getSocket().close();
            } catch (IOException e) {throw new RuntimeException("could not close sockets");}
        });
        serverSocket.close();
        System.out.println("Server closed successfully");
    }



}

