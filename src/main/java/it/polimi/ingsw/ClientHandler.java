package it.polimi.ingsw;

import it.polimi.ingsw.messages.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandler {
    int port;
    String model;
    Scanner inStream;
    ObjectOutputStream outStream;
    ServerSocket serverSocket;
    List<VirtualView> views;
    //To the first client we ask the number of players, which is the first thing inside the gamecontroller
    public ClientHandler(int port) {
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
        new NoReplyMessage("OK! Waiting players for a "+n+"-player game...").send(client1);

        lookForMorePlayers((n-1));
        System.out.println(views);
        Thread pingSender = new Thread(this::startPing);
        pingSender.start();
        return n;
    }
    public void lookForMorePlayers(int n) throws IOException {
        for (int i=0; i<n ; i++){
            Socket socket = serverSocket.accept();
            VirtualView clientView = new VirtualView(socket,i+2);
            views.add(clientView);
            new LoginMessage("joining an existing game ... ("+(n+1)+"-player game)" ).send(clientView);
            System.out.println("Connected to client! We are at " + (i+2) + " players out of " + (n+1));
        }
        try{Thread.sleep(100);}catch(Exception ex){}
    }


    private int askForPN(VirtualView client)  {
        int input = 0;
        while ((input != 3) && (input != 2)) {
                new FirstClientMessage("Welcome! How many players?").send(client);
                input = Integer.parseInt(client.getReply());
            System.out.println("received reply: "+ input);
        }
        return input;
    }


    private void startPing() {
        while (true){
            try {
                new PingMessage().send(views);
                Thread.sleep(2000);
            }catch (InterruptedException ex) {
                System.err.println("cannot send Ping!");
            }
        }
    }
    public void closeEverything() throws IOException {
        views.forEach(v-> {
            try {
                v.getSocket().close();
            } catch (IOException e) {throw new RuntimeException("could not close sockets");}
        });
        serverSocket.close();
        System.out.println("Server closed successfully");
    }



}

