package it.polimi.ingsw.CLIENT;

import java.io.*;
import java.net.Socket;

import java.util.Scanner;

public class NetworkHandler{


    final Socket socket;
    View view;

    String model;

    public NetworkHandler() throws IOException {
        socket = new Socket("localhost",1337);
        System.out.println("connected to server at 1337");
    }

    public void startClient(View view) throws IOException{
        try (socket) {
            PrintWriter outSocket = new PrintWriter(socket.getOutputStream());
            Scanner socketScanner = new Scanner(socket.getInputStream());
            while (true) {
                String socketInput = socketScanner.nextLine();
                model = socketInput;
                view.update(this);
            }
        }
    }

}
