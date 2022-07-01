package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.Repliable;
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
    Boolean gameOver = false;
    private Socket socket;
    private final Boolean GUI;
    Thread listener;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Consumer<Message> messageArrivedObserver;
    private final List<Message> delayedMessages = new ArrayList<>();
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
            try {
                Timer timeout = new Timer(6000,onTimeout);
                timeout.setRepeats(false);
                while (true) {
                    message = (Message) inStream.readObject();
                    if (message.isPing()){
                        timeout.restart();
                        if (disconnected){
                            pingCounter++;
                            if (pingCounter>=5){pingCounter=0;setDisconnected(false);
                                System.out.println("Reconnected!");}
                        }
                        else{
                        synchronized (socket) {
                            //we send again the same ping message.
                                outStream.writeObject(message);
                                outStream.flush();
                            }
                        }
                    }
                    else {
                        if (message.isRepliable()){
                            if(!disconnected) {
                                this.currentMessage = message;
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


    /**
     * This will be called when the disconnection timer expires.
     * We set disconnected to true and tell the user that they are disconnected.
     */
    ActionListener onTimeout = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameOver){return;}
            setDisconnected(true);
            System.err.println("Server is not responding. ");
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

    public void endConnectionandGame(){
        try{
            outStream.close();
            inStream.close();
            socket.close();
        }catch (Exception exception){exception.printStackTrace();}
        gameOver = true;
    }

    public void setDisconnected(Boolean disconnected) {
        this.disconnected = disconnected;
    }
}
