package it.polimi.ingsw.CLIENT;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Welcome! Starting the client...");
        NetworkHandler nh = new NetworkHandler();
        View View = new View(nh);
        nh.startClient(View);
    }
}
