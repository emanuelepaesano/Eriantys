package it.polimi.ingsw;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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

    public void update(Object model){
        try {
            outStream.writeObject(model);
            outStream.flush();
        }catch (IOException ex){ex.printStackTrace();}
    }


    public Message getAnswer()  {
        try {
            return (Message) inStream.readObject();
        }catch ( IOException| ClassNotFoundException ex ){throw new RuntimeException();}
    }

    public int getPlayerId() {
        return playerId;
    }

    public Socket getSocket() {
        return socket;
    }
}
