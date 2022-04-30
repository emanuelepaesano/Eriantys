package it.polimi.ingsw.CLIENT;

import java.io.*;
import java.net.Socket;

public class NetworkHandler{


    final Socket socket;
    View view;

    ViewState currentState;
    //dobbiamo mandargli i parametri per creare il prossimo stato

    public NetworkHandler() throws IOException {
        socket = new Socket("localhost",1337);
        System.out.println("connected to server at 1337");
    }

    // TODO: 01/05/2022 Multithreading (one for every client)
    public void startClient(View View) throws IOException, ClassNotFoundException{
        this.view = View;
        PrintWriter outSocket = new PrintWriter(socket.getOutputStream());
        ObjectInputStream socketReader = new ObjectInputStream(socket.getInputStream());
        System.out.println("arrivato a try-socket");
        try (socket) {
            Object socketInput = socketReader.readObject();
            View.update(new InitState(socketInput)); //ask pl. number or empty
            if (!socketInput.equals("")){
                outSocket.println(View.getUserInput());
                outSocket.flush();
                System.out.println(socketReader.readObject()); //confirmation
            }
            else{System.out.println("joining an existing game...");}
            socketInput = socketReader.readObject();
            System.out.println(socketInput); //game start
            while (true) {
                socketInput = socketReader.readObject(); //main client loop. we wait for objects and update the view
                System.out.println(socketInput);
                outSocket.println(view.getUserInput());
                outSocket.flush();
//                currentState = view.getCurrentView();
//                ViewState newView = currentState.nextState(socketInput);
//                view.update(newView);
//                outSocket.println(view.getUserInput());
            }
        }
    }


}
