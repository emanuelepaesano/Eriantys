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
    Scanner inStream;
    ObjectOutputStream outStream;
    //ONE VIRTUAL VIEW FOR EACH PLAYER. THIS IS A CLIENT HANDLER

    //could also work with the playername
    VirtualView(Socket socket, int id) throws IOException {
        inStream = new Scanner(socket.getInputStream());
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

    public void update(){
        //this just send the whole model(?)
    }

    public String getAnswer(){
        return inStream.nextLine();
    }

    public int getPlayerId() {
        return playerId;
    }
}
