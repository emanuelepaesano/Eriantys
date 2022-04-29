package it.polimi.ingsw;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.controller.GameController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerHandler {
    int port;
    List<Socket> clients;
    String model;
    Scanner inStream;
    PrintWriter outStream;
    ServerSocket serverSocket;
    //To the first client we ask the number of players, which is the first thing inside the gamecontroller
    public ServerHandler(int port) {
        this.port = port;
        clients = new ArrayList<>();
    }

    // TODO: 29/04/2022 obviously this will need to handle more than 1 client
    public void startServer() throws IOException{

        try {serverSocket = new ServerSocket(port);}
        catch (IOException e) {System.err.println(e.getMessage()); return;}
        System.out.println("Server ready");

        Socket socket = serverSocket.accept();
        clients.add(socket);
        System.out.println("Connected!");
        try {
            inStream = new Scanner(socket.getInputStream());
            outStream = new PrintWriter(socket.getOutputStream());
            } catch(IOException e) {
                e.printStackTrace();
            }
    }

    public void lookForMorePlayers(int n) throws IOException {
        for (int i=0; i<n ; i++){
            Socket socket = serverSocket.accept();
            clients.add(socket);
            System.out.println("Connected! We are at " + (i+2) + " players out of " + (n+1));
        }
        System.out.println("We can start the game! ("+ (n+1) +"-player game)");
    }

    public void update(String model){
        outStream.println(model);
        outStream.flush();
    }

    public String getAnswer(){
        return inStream.nextLine();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        GameController gc = new GameController();

    }

}

