package it.polimi.ingsw.CLIENT;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkHandler{
    //possiamo fare altrimenti una sorta di local model che si aggiorna quando arriva qualcosa.
    //Come questo si aggiorna viene chiamato view.update() che fa vedere cosa c'e ora nel modello.
    //possiamo fare anche che il modello ha diversi stati, ma in qualsiasi stato può sempre ricevere
    //una stringa di "input veloce"(tipo popup) dal server. Ma poi nel gioco non dovrebbe essere proprio cosi
    //c'e i dati arrivano solo a seconda di cosa fa l'utente, non a piacimento del server.
    //possiamo però lasciarci questo spazio a parte dagli stati della view. L'idea sarebbe
    //che queste cose appaiono come popup sopra alla view che resta però allo stato attuale

    final Socket socket;
    View view;

    ViewState currentState;
    //dobbiamo mandargli i parametri per creare il prossimo stato

    public NetworkHandler() throws IOException {
        socket = new Socket("localhost",1337);
        System.out.println("connected to server at 1337");
    }


    public void startClient(View View) throws IOException, ClassNotFoundException{
        this.view = View;
        PrintWriter outSocket = new PrintWriter(socket.getOutputStream());
        ObjectInputStream socketReadr = new ObjectInputStream(socket.getInputStream());
        System.out.println("arrivato a try-socket");
        try (socket) {
            Object socketInput = socketReadr.readObject();
            //ask pl. number or empty
            if (!socketInput.equals("")){
                outSocket.println(View.getUserInput());
                outSocket.flush();
                System.out.println(socketReadr.readObject()); //confirmation
            }
            else{System.out.println("joining an existing game...");}
            socketInput = socketReadr.readObject();
            System.out.println(socketInput); //game start
            while (true) {
                socketInput = socketReadr.readObject(); //main client loop. we wait for objects and update the view
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


    public synchronized void startClientWithThreads(View view, LocalModel model){
        this.view = view;

        Thread listener = new Thread(()->{
            ObjectInputStream socketReader;
            Object socketInput;
            try {
                socketReader = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    //this is far from ideal. i did this because we can send 3 things which are
                    //only generalview, view, or string (message)
                    socketInput = socketReader.readObject();
                    try {
                        System.out.println("trying to cast 1");
                        model.setGameState((GeneralViewState) socketInput);
                        model.setCurrentState((ViewState) socketInput);
                        view.update();
                    } catch (ClassCastException notagenview) {
                        try {
                            System.out.println("trying to cast 2");
                            model.setCurrentState((ViewState) socketInput);
                            view.update();

                        } catch (ClassCastException message) {
                            System.out.println("message: \n");
                            model.setMessage(socketInput.toString());
                            view.getMessage();
                        }
                    }
                }
            }catch (IOException | ClassNotFoundException e) {throw new RuntimeException(e);}
        });


        Thread speaker = new Thread(()->{
            PrintWriter outSocket;
            try {
                outSocket = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {throw new RuntimeException(e);}

            while (true){
                String userInput = view.getUserInput();
                Boolean ToSend = checkString(userInput, model);
                if (ToSend){
                    outSocket.println(userInput);
                    outSocket.flush();}
            }
        });
        listener.start();
        speaker.start();
    }

    private Boolean checkString(String inputString, LocalModel model){
        if (inputString.equalsIgnoreCase("view")){
            model.getGameState().display();
            return false;
        }
        else {return true;}
    }
}
