package it.polimi.ingsw.CLIENT;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome! Starting the client...");
        View view = new View();
        NetworkHandler nh = new NetworkHandler();
        nh.startClient(view);
    }
}
