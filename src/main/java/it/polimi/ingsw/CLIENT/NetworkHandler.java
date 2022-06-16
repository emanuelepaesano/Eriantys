package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.messages.PingMessage;
import it.polimi.ingsw.messages.Repliable;

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
    Boolean GUI;
    Thread listener;
    ObjectInputStream inStream;
    ObjectOutputStream outStream;
    private Consumer<Message> messageArrivedObserver;
    private List<Message> delayedMessages = new ArrayList<>();
    private Message currentMessage;


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
                        timeout.restart();
                        outStream.writeObject(new PingMessage());
                        outStream.flush();
                    }

                    else {
                        System.out.println("last non ping message: " + message.getClass().getSimpleName());
                        if (message.isRepliable()){
                            this.currentMessage = message;
                            System.out.println("current message: " +currentMessage);
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

    public void sendMessage(String content) {
    if (currentMessage != null){sendReply(content);}
    else try {
            outStream.writeObject(new NoReplyMessage(content));
            outStream.flush();
            outStream.reset();
        } catch (IOException e) {
            System.err.println("Could not send message...");
        }
    }

    public void sendReply(String reply){
        try {
            ((Repliable) currentMessage).setReply(reply);
            System.out.println("sending reply");
            outStream.writeObject(currentMessage);
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
                if (GUI){UIManager.getUIManager().getMainWindow().close();}
                outStream.close();
                inStream.close();
                socket.close();


            } catch (IOException ex) {
                throw new RuntimeException("could not close socket");
            }
        }
    };

}
