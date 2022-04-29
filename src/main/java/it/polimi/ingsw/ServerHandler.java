package it.polimi.ingsw;

import it.polimi.ingsw.CLIENT.NetworkHandler;

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
    //To the first client we ask the number of players, which is the first thing inside the gamecontroller
    public ServerHandler(int port) {
        this.port = port;
        clients = new ArrayList<>();
    }

    // TODO: 29/04/2022 obviously this will need to handle more than 1 client
    public void startServer() throws IOException, InterruptedException{
        ServerSocket serverSocket;
        try {serverSocket = new ServerSocket(port);}
        catch (IOException e) {System.err.println(e.getMessage()); return;}
        System.out.println("Server ready");
        while(clients.size()<3) {
            Socket socket = serverSocket.accept();
            clients.add(socket);
            System.out.println("Connected!");
        }
        while (true) {
            try {
                //with this we can send stuff to 1 client only
                PrintWriter outStream = new PrintWriter(clients.get(0).getOutputStream());
                outStream.println("Look i'm sending the model!");
                outStream.flush();
                Thread.sleep(5000);
            } catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Closing the server ... ");
        serverSocket.close();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        ServerHandler sh = new ServerHandler(1337);
        sh.startServer();

    }
}

