package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.CLIENT.ViewImpls.CLIView;
import it.polimi.ingsw.CLIENT.ViewImpls.LoginView;
import it.polimi.ingsw.CLIENT.ViewImpls.WaitingView;
import it.polimi.ingsw.messages.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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

    public NetworkHandler(UserView userView) throws IOException {
        this.view = userView;
        chooseUI();
        try {
            synchronized (this) { wait();}
        }catch(InterruptedException ex){}
        initializeViews();
        assignFirstView();
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
            this.cliView = new CLIView(this);
        }
        else{
            this.waitingView = new WaitingView();
            this.loginView = new LoginView(this);
            //and all the other views for the gui
        }

    }

    private void assignFirstView(){
        if(!GUI){view.setCurrentView(this.cliView);}
        else {view.setCurrentView(this.waitingView);}
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
                        selectAndFillView(message);
                        System.out.println("filled view");
                        view.update();
                        System.out.println("updated view");
                    }
                }
            }catch (IOException | ClassNotFoundException e) {System.err.println("Connection lost.");}
        });


//        Thread speaker = new Thread(()->{
//            try {
//                outStream = new ObjectOutputStream(socket.getOutputStream());
//                while (true){
//                    Message userInput = userView.getUserInput();
//                    System.out.println("input read");
//                    if(userInput.toString().equalsIgnoreCase("stopgame")){
//                        System.out.println("read stopgame");
//                        break;
//                    }
//                    else {
//                    outStream.writeObject(userInput);
//                    outStream.flush();
//                    }
//                }
//            } catch (IOException e) {
//                System.err.println("Connection lost.");
//            }
//        });
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


    private void selectAndFillView(Message message){
        if (!GUI){
            view.getCurrentView().fillInfo(message);
        }
        else switch (message.getView()){
            case "loginview":
                // TODO: 05/05/2022 we should not make a new one
                //  but use an already made one and fill it with message info
                view.setCurrentView(this.loginView);
                view.currentView.fillInfo(message);
            case "planningview": //from planningphasemessage
            case "actionview": //from actionphasemessage
            case "cloudselection": //from cloudmessage
            case "generalview": //from generalviewmessage
            case "simpleview": //from stringmessage //this can just be an overlaying popup or something similar
        }
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
