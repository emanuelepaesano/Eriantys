package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.CLIENT.ViewImpls.CLIView;
import it.polimi.ingsw.CLIENT.ViewImpls.LoginView;
import it.polimi.ingsw.messages.Message;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    UserView view;
    Boolean GUI;
    CLIView cliView;
    LoginView loginView;

    View currentState;
    //dobbiamo mandargli i parametri per creare il prossimo stato

    public NetworkHandler() throws IOException {
        chooseUI();
        try {
            synchronized (this) { wait();}
        }catch(InterruptedException ex){}
        initializeViews();
        socket = new Socket("localhost",1337);
        System.out.println("connected to server at 1337");
    }


    private void chooseUI(){
        JFrame frame = new JFrame("Interface");
        frame.setSize(300,300);
        JButton b1 = new JButton("Graphic Interface");
        JButton b2 = new JButton("Command Line");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new FlowLayout());
        frame.add(b1);
        frame.add(b2);
        frame.pack();
        b1.addActionListener(e-> {GUI =true; frame.setVisible(false);synchronized (this){notifyAll(); }});
        b2.addActionListener(e-> {GUI =false; frame.setVisible(false);synchronized (this){notifyAll();}});
        frame.setVisible(true);

    }
    public void initializeViews(){
        if (!GUI){
            this.cliView = new CLIView();
        }
        else{
            this.loginView = new LoginView();
            //and all the other views for the gui
        }

    }
    public synchronized void startClientWithThreads(UserView userView){
        this.view = userView;

        //per fare cosi bisogna capire quale tipo di messaggio è in arrivo però. come lo facciamo?
        Thread listener = new Thread(()->{
            ObjectInputStream socketReader;
            Message socketInput;
            try {
                socketReader = new ObjectInputStream(socket.getInputStream());

                while (true) {
                    socketInput = (Message) socketReader.readObject();
                    selectAndFillView(socketInput);
                    userView.update();
                }
            }catch (IOException | ClassNotFoundException e) {throw new RuntimeException("no good");}
        });


        Thread speaker = new Thread(()->{
            ObjectOutputStream outSocket;
            try {
                outSocket = new ObjectOutputStream(socket.getOutputStream());

            while (true){
                Message userInput = userView.getUserInput();
                outSocket.writeObject(userInput);
                outSocket.flush();
            }
            } catch (IOException e) {throw new RuntimeException(e);}
        });
        listener.start();
        speaker.start();
    }

    private void selectAndFillView(Message message){
        if (!GUI){
            view.getCurrentView().fillInfo(message);
        }
        switch (message.getView()){
            case "loginview":
                view.setCurrentView(new LoginView());
                view.currentView.fillInfo(message);
            case "planningview": //from planningphasemessage
            case "actionview": //from actionphasemessage
            case "generalview": //from generalviewmessage
            case "simpleview": //from stringmessage
        }
    }
}
