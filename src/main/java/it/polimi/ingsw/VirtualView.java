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

/**
 * ONE VIRTUAL VIEW FOR EACH PLAYER
 */
public class VirtualView {
    private final int playerId;
    private final Socket socket;
    ObjectInputStream inStream;
    ObjectOutputStream outStream;

    ServerStarter server;
    Timer timeOut;
    private String reply = "wait";

    private Boolean disconnected = false;

    /**
     * Each Virtual View handles the socket connection with one client instance.
     * @param server a reference to the rest of the server, to notify others if this connection fails.
     * @param socket the socket object through which we will send and receive messages with the client.
     * @param id each Virtual View is assigned an id number.
     */
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

    /**
     * Method used to send a message to the client. It is called
     * directly from the message class.
     * @param message the message to send to the client.
     */
    public synchronized void update(Message message){
        try {
            outStream.writeObject(message);
            outStream.flush();
            outStream.reset();
        }catch (IOException ex){ex.printStackTrace();}
    }

    /**
     * The connection thread will receive Message objects from the client, that contain a reply as a String.
     * When this happens, the reply field of this is updated and the main thread (this one) is notified. Every time the
     * Server needs an input from the player, the main thread will end up here and wait for the connection
     * thread to provide it with a reply.
     * @return the reply from the client, as a string.
     * @throws DisconnectedException if the main thread is notified as a result of a disconnection,
     * we will throw a DisconnectedException instead.
     */
    public String getReply() throws DisconnectedException {
        while (reply.equals("wait")){
            if (isDisconnected()){throw new DisconnectedException();}
            try{wait();}catch (Exception ignored){}
        }
        String str = reply;
        reply = "wait";
        return str;
    }

    /**
     * This is the method executed by the connection listener thread.
     * This method waits for incoming messages and acts accordingly to the type of
     * message received. Every time a ping message is received, a "disconnection timer" is reset
     * as a signal that the connection is still alive.
     * If the message is not a ping, we expect it to be a Repliable, from which we can extract the
     * reply field, and update the main thread.
     */
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
                else {
                    if (message.isPing()){
                        if (disconnected) {
                            setDisconnected(false);
                            server.aViewReconnected(this);
                        }
                        timeOut.restart();
                    }
                }
            } catch (IOException | ClassNotFoundException exc){
                System.out.println("From connection thread: the stream was closed");
                break;
            }
        }
    }

    /**
     * This method is executed by the other thread working on the socket output stream, besides the main.
     * This thread simply sends a ping message every second.
     */
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
            }catch (IOException | InterruptedException ex) {
                System.err.println("cannot send Ping!");
                break;
            }
        }
    }

    /**
     * Method called when the disconnection timer finished, meaning that the ping messages did not arrive in time
     * to reset it. This means that we are now disconnected from the client.
     * The other clients will be notified by their respective Virtual Views.
     */
    ActionListener onTimeout = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("there was a disconnection");
            //this must be synchronized with isDisconnected, since main and other thread(this) will access it
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
