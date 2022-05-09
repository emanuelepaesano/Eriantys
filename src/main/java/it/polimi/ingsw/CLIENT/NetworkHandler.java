package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.CLIENT.ViewImpls.CLIView;
import it.polimi.ingsw.CLIENT.ViewImpls.LoginView;
import it.polimi.ingsw.CLIENT.ViewImpls.WaitingView;
import it.polimi.ingsw.messages.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NetworkHandler{
    //possiamo fare altrimenti una sorta di local model che si aggiorna quando arriva qualcosa.
    //Come questo si aggiorna viene chiamato view.update() che fa vedere cosa c'e ora nel modello.
    //possiamo fare anche che il modello ha diversi stati, ma in qualsiasi stato può sempre ricevere
    //una stringa di "input veloce"(tipo popup) dal server. Ma poi nel gioco non dovrebbe essere proprio cosi
    //c'e i dati arrivano solo a seconda di cosa fa l'utente, non a piacimento del server.
    //possiamo però lasciarci questo spazio a parte dagli stati della view. L'idea sarebbe
    //che queste cose appaiono come popup sopra alla view che resta però allo stato attuale

    Socket socket;
    UserView view;
    Boolean GUI;
    CLIView cliView;
    private WaitingView waitingView;
    LoginView loginView;

    View currentState;
    //dobbiamo mandargli i parametri per creare il prossimo stato

    Thread listener;
    Thread speaker;
    ObjectInputStream inStream;
    ObjectOutputStream outStream;
    private Consumer<Message> messageArrivedObserver;
    private List<Message> delayedMessages = new ArrayList<>();
    private UIManager UIManager;


    public NetworkHandler(Boolean GUI, UserView view) {
        this.GUI = GUI;
        this.view = view;
    }

    void startConnection(){
        while(true){
            try {
                socket = new Socket("localhost", 1337);
                System.out.println("connected to server at 1337");
                this.outStream = new ObjectOutputStream(socket.getOutputStream());
                this.inStream = new ObjectInputStream(socket.getInputStream());
                break;
            }catch (IOException ex){
                System.err.println("server is not online");
                try{Thread.sleep(3000);}catch(Exception ecc){}
            }
        }
    }

    public synchronized void startListenerThread(){
        Thread listener = new Thread(()->{
            Message message;
            System.out.println("listener started");
            try {
                System.out.println("waiting for messages");
                Timer timeout = new Timer(5000,onTimeout);
                timeout.setRepeats(false);
                while (true) {
                    message = (Message) inStream.readObject();
                    if (message.isPing()){
                        timeout.restart();}
                    else {
                        if (GUI) {notifyMessageArrived(message);}
                        else {
                            view.getCurrentView().fillInfo(message);
                            view.update();
                        }
                    }
                }
            }catch (IOException | ClassNotFoundException e) {System.err.println("Connection lost.");}
        });

        this.listener = listener;
        listener.start();
    }

    public void sendMessage(Message message)
    {
        try {
            outStream.writeObject(message);
            outStream.flush();
            outStream.reset();
        } catch (IOException e) {
            System.err.println("Could not send message...");
        }
    }

    synchronized public void setMessageArrivedObserver(Consumer<Message> messageArrivedObserver)
    {
        this.messageArrivedObserver = messageArrivedObserver;
        for (Message msg: delayedMessages) {
            messageArrivedObserver.accept(msg);
        }
        delayedMessages.clear();
    }

    synchronized public void notifyMessageArrived(Message msg)
    {
        if (messageArrivedObserver != null)
            messageArrivedObserver.accept(msg);
        else
            delayedMessages.add(msg);
    }



    ActionListener onTimeout = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                System.err.println("Server is not responding. Closing the game...");
                outStream.close();
                inStream.close();
                socket.close();


            } catch (IOException ex) {
                throw new RuntimeException("could not close socket");
            }
        }
    };

}
