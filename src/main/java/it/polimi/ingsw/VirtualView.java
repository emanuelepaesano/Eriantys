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
import java.net.SocketException;

public class VirtualView {
    private final int playerId;
    private final Socket socket;
    ObjectInputStream inStream;
    ObjectOutputStream outStream;

    ServerStarter server;
    Timer timeOut;
    private String reply = "wait";

    private Boolean disconnected = false;
    //ONE VIRTUAL VIEW FOR EACH PLAYER

    //could also work with the playername
    VirtualView(ServerStarter server, Socket socket, int id) throws IOException {

        this.server = server;
        inStream = new ObjectInputStream(socket.getInputStream());
        System.out.println("input stream " +id + " ok");
        outStream = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("output stream " +id + " ok");
        this.playerId = id;
        this.socket = socket;
        new Thread(this::startConnection).start();
        new Thread(this::startPing).start();
    }

    public synchronized void update(Message message){
        try {
            outStream.writeObject(message);
            outStream.flush();
            outStream.reset();
        }catch (IOException ex){ex.printStackTrace();}
    }

    public void onClientDisconnection(){
        //dovremmo interrompere tutto e mandare un messaggio ai client
        //mandiamo prima quindi i messaggi che uno si è disconnesso,
        //poi stoppiamo tutti i task sul server, una cosa del genere
    }


    public String getReply() throws DisconnectedException {
        while (reply.equals("wait")){
            if (isDisconnected()){throw new DisconnectedException();}
            try{wait();}catch (Exception ex){}
        }
        String str = reply;
        reply = "wait";
        return str;
    }

    private void startConnection() {
        timeOut = new Timer(7000, onTimeout);
        timeOut.setRepeats(false);
        while (true){
            Message message;
            try {
                message = (Message) inStream.readObject();
                if (message.isRepliable()){
                    this.reply = ((Repliable) message).getReply();
                    synchronized (this){notifyAll();}
                }
                else {if (message.isPing()){
                    if (disconnected) {
                        setDisconnected(false);
                        server.aViewReconnected(this);
                    }
                    timeOut.restart();
                }}
            } catch (IOException | ClassNotFoundException exc){
                System.out.println("From connection thread: the stream was closed");
            }
        }

    }

    private void startPing() {
        while (true){
            try {
                synchronized (this) {
                    if(!disconnected) {
                        outStream.writeObject(new PingMessage());
                        outStream.flush();
                    }
                }
                Thread.sleep(1000);
                //se la view è disconnessa, per fortuna continuiamo a mandare i ping, ma
                //gli arriveranno tutti insieme quando si riconnette. quindi aspettiamo
                //a mandare finche non si riconnette, tanto avrà già i ping di prima a cui
                //rispondere (?)

            }catch (IOException | InterruptedException ex) {
                System.err.println("cannot send Ping!");
                break;
            }
        }
    }
    ActionListener onTimeout = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("there was a disconnection");
            //this thing must be synchronized with isDisconnected, since main and other thread(this) will access it
            setDisconnected(true);

            //send message to other clients
            server.aViewDisconnected(getThis());

            //we must tell the thread that was waiting that nothing will arrive.
            //So it can raise an exception
            synchronized (this){notifyAll();}
        }
    };

    //this is only for the ActionListener.
    private VirtualView getThis(){return this;}

    public int getPlayerId() {
        return playerId;
    }

    public Socket getSocket() {
        return socket;
    }

    public synchronized Boolean isDisconnected(){
        return disconnected;
    }
    public synchronized void setDisconnected(Boolean disconnected) {
        this.disconnected = disconnected;
    }
}
