package it.polimi.ingsw;



import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.Repliable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VirtualView {
    private final int playerId;
    private final Socket socket;
    ObjectInputStream inStream;
    ObjectOutputStream outStream;
    //ONE VIRTUAL VIEW FOR EACH PLAYER. THIS IS A CLIENT HANDLER

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
        }catch (IOException ex){ex.printStackTrace();}
    }

    Runnable getHeartBeat = new Runnable() {
        @Override
        public void run() {
            while(true){
                try {
                    Message pingMessage = (Message) inStream.readObject();
                    Thread.sleep(6000);
                } catch (ClassNotFoundException |InterruptedException e) {throw new RuntimeException(e);} catch (IOException e) {
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

//    public Consumer<String> replyTo;

//    public void setReplyTo(Consumer<String> replyTo) {
//        this.replyTo = replyTo;
//    }

    public int getPlayerId() {
        return playerId;
    }

    public Socket getSocket() {
        return socket;
    }
}
