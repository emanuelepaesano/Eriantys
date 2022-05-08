package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.CLIENT.ViewImpls.CLIView;
import it.polimi.ingsw.CLIENT.ViewImpls.LoginView;
import it.polimi.ingsw.CLIENT.ViewImpls.WaitingView;
import it.polimi.ingsw.messages.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NetworkHandler extends Application {
    //possiamo fare altrimenti una sorta di local model che si aggiorna quando arriva qualcosa.
    //Come questo si aggiorna viene chiamato view.update() che fa vedere cosa c'e ora nel modello.
    //possiamo fare anche che il modello ha diversi stati, ma in qualsiasi stato può sempre ricevere
    //una stringa di "input veloce"(tipo popup) dal server. Ma poi nel gioco non dovrebbe essere proprio cosi
    //c'e i dati arrivano solo a seconda di cosa fa l'utente, non a piacimento del server.
    //possiamo però lasciarci questo spazio a parte dagli stati della view. L'idea sarebbe
    //che queste cose appaiono come popup sopra alla view che resta però allo stato attuale

    Socket socket;
    UserView view;
    public static Boolean GUI;
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
    private GUIManager GUIManager;

    public void enableWaitingView(){
        if (waitingView ==null){
            waitingView = new WaitingView();
        }
        waitingView.display();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        chooseUI();
        view = new UserView();
        if (GUI){
            enableWaitingView();
            Task<Void> task = new Task<>() {
                @Override
                public Void call(){
                    startConnection();
                    return null;
                }
            };
            task.setOnSucceeded((e)->{
                waitingView.close();
                this.GUIManager = new GUIManager(stage);
                startListenerThread();
            });
            new Thread(task).start();
        }
        else {cliView = new CLIView(this);
            StartCLI();
            startConnection();
            startListenerThread();
            Platform.exit();
        }
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

    void chooseUI(){
        Stage stage = new Stage();
        stage.setTitle("Choose an interface type.");
        Button b1 = new Button("Graphic Interface");
        Button b2 = new Button("Command Line");
        HBox layout = new HBox();
        layout.getChildren().addAll(b1,b2);
        Scene scene = new Scene(layout,300,300);
        stage.setScene(scene);
        stage.sizeToScene();
        b1.setOnAction(e-> {GUI =true; stage.close();});
        b2.setOnAction(e-> {GUI =false; stage.close();});
        stage.showAndWait();
    }

    public void initializeViews(){
        if (!GUI){
            this.cliView = new CLIView(this);
        }
        else{
            this.waitingView = new WaitingView();
            this.loginView = new LoginView();
            //and all the other views for the gui
        }

    }
    void StartCLI(){
        if(!GUI){view.setCurrentView(this.cliView);}
    }

    public synchronized void startListenerThread(){
        Thread listener = new Thread(()->{
            Message message;
            System.out.println("listener started");
            try {
                System.out.println("waiting for messages");
                Timer timeout = new Timer(5000,onTimeout);
                timeout.setRepeats(false);
                if (GUI) {setMessageArrivedObserver((msg)->GUIManager.getGuiManager().selectAndFillView(msg, this));}
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
//        this.speaker = speaker;
        listener.start();
//        speaker.start();
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
