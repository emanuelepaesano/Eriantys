package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.messages.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;

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

    private Socket socket;
    private final Boolean GUI;
    Thread listener;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Consumer<Message> messageArrivedObserver;
    private List<Message> delayedMessages = new ArrayList<>();
    private Message currentMessage;

    private Boolean disconnected = false;


    public NetworkHandler(Boolean GUI) {
        this.GUI = GUI;
    }

    void startConnection(String ip, int port){
        while(true){
            try {
                socket = new Socket(ip, port);
                System.out.println("connected to server at " + port);
                this.outStream = new ObjectOutputStream(socket.getOutputStream());
                this.inStream = new ObjectInputStream(socket.getInputStream());
                break;
            }catch (IOException ex){
                System.err.println("server is not online. Waiting for server . . . ");
                try{Thread.sleep(3000);}catch(Exception exception){exception.printStackTrace();}
            }
        }
    }

    public synchronized void startListenerThread(){
        Thread listener = new Thread(()->{
            int pingCounter = 0;
            Message message;
            System.out.println("listener started");
            try {
                System.out.println("waiting for messages");
                Timer timeout = new Timer(6000,onTimeout);
                timeout.setRepeats(false);
                while (true) {
                    message = (Message) inStream.readObject();
                    if (message.isPing()){
                        timeout.restart();
                        if (disconnected){
                            pingCounter++;
                            System.out.println("ping for reconnection number: " + pingCounter);
                            if (pingCounter>=5){pingCounter=0;setDisconnected(false);
                                System.out.println("reconnected.");}
                        }
                        else{
                        synchronized (socket) {
                            //and send again the same ping message.
                                outStream.writeObject(message);
                                outStream.flush();
                            }
                        }
                    }

                    else {
                        System.out.println("last non ping message: " + message.getClass().getSimpleName());
                        if (message.isRepliable()){
                            if(!disconnected) {
                                this.currentMessage = message;
                                System.out.println("current message: " + currentMessage);
                            }
                        }
                        if (GUI) {
                            notifyMessageArrived(message);
                        }
                        else {
                            UIManager.getUIManager().getCliView().fillInfo(message);
                            UIManager.getUIManager().getCliView().display();
                        }
                    }
                }
            }catch (IOException | ClassNotFoundException e) {System.err.println("Connection lost.");}
        });
        this.listener = listener;
        listener.start();
    }



    public void sendReply(String reply){
        try {
            ((Repliable) currentMessage).setReply(reply);
            System.out.println("sending reply");
            synchronized (socket) {
                outStream.writeObject(currentMessage);
                outStream.flush();
                outStream.reset();
            }
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

    synchronized public void notifyMessageArrived(Message msg) {
        if (disconnected){return;}
        if (messageArrivedObserver != null) {
            // this code is to test disconnections. Uncomment it in that case
//            if (msg.getClass().getSimpleName().equals("ActionPhaseMessage")) {
//                ActionPhaseMessage message = (ActionPhaseMessage) msg;
//                if ((message.getPlayer().getId() == 1 ||message.getPlayer().getId() == 3)  && message.getType().equals(TEST)) {
//                    System.out.println("stopping connection");
//                    try {
//                        Thread.sleep(30000);
//                    } catch (Exception exc) {
//                        System.out.println("sleep problem");
//                    }
//                    System.out.println("now i will resume connection");
//                }
//            }
            //until here
            messageArrivedObserver.accept(msg);
        }
        else {delayedMessages.add(msg);}
    }



    ActionListener onTimeout = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.err.println("Server is not responding. ");
            setDisconnected(true);
            if (GUI) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Disconnection");
                    alert.setHeaderText("Disconnection");
                    alert.setContentText("""
                            You are offline.
                            If you don't reconnect and more than 1 player remains, the game will continue without you.
                            Otherwise the last player left will win in 45 seconds.""");
                    alert.setHeight(600);
                    alert.setWidth(800);
                    alert.showAndWait();
                });
            }
            else System.out.println("""
                            You are offline.
                            If you don't reconnect and more than 1 player remains, the game will continue without you.
                            Otherwise the last player left will win in 45 seconds.""");
        }
    };

    public Thread getListener() {
        return listener;
    }

    public void setDisconnected(Boolean disconnected) {
        this.disconnected = disconnected;
    }
}
