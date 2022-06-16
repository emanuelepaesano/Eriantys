package it.polimi.ingsw;



import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PingMessage;
import it.polimi.ingsw.messages.Repliable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VirtualView {
    private final int playerId;
    private final Socket socket;
    ObjectInputStream inStream;
    ObjectOutputStream outStream;

    Timer timeOut;
    //ONE VIRTUAL VIEW FOR EACH PLAYER

    //could also work with the playername
    VirtualView(Socket socket, int id) throws IOException {
        inStream = new ObjectInputStream(socket.getInputStream());
        System.out.println("input stream " +id + " ok");
        outStream = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("output stream " +id + " ok");
        this.playerId = id;
        this.socket = socket;
    }

    public synchronized void update(Message message){
        try {
            outStream.writeObject(message);
            outStream.flush();
            outStream.reset();
        }catch (IOException ex){ex.printStackTrace();}
    }

    Runnable getHeartBeat = new Runnable() {
        //cosi non funzionerebbe perche l'altro thread che legge i messaggi tira una ioexception
        @Override
        public void run() {
            while(true){
                try {
                    Message pingMessage = (Message) inStream.readObject();
                    Thread.sleep(6000);
                } catch (ClassNotFoundException |InterruptedException e) {throw new RuntimeException(e);}
                catch (IOException e) {
                    onClientDisconnection();
                }
            }
        }
    };
    public void onClientDisconnection(){
        //dovremmo interrompere tutto e mandare un messaggio ai client
        //mandiamo prima quindi i messaggi che uno si è disconnesso,
        //poi stoppiamo tutti i task sul server, una cosa del genere
    }


    public String getReply()  {
        try {
            Message userMessage = (Message) inStream.readObject();
            if ((userMessage).isRepliable()) {
//                    replyTo.accept(((Repliable)userMessage).getReply());
                    return ((Repliable)userMessage).getReply();
            }
            else{throw new RuntimeException("Violation of protocol");}
        }catch ( IOException| ClassNotFoundException ex ){throw new RuntimeException("could not get answer");}
    }

    private void startConnection(){
        timeOut = new Timer(7000, onTimeout);
        Timer forPing = new Timer(100, sendPing);
        forPing.start();

    }

    ActionListener onTimeout = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("there was a disconnection");
            //send message to other clients
        }
    };

    ActionListener sendPing = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            update(new PingMessage());
            try {
                Message ping = (Message)inStream.readObject();
                if (ping.isPing()){
                    timeOut.restart();
                }
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    public int getPlayerId() {
        return playerId;
    }

    public Socket getSocket() {
        return socket;
    }
}
