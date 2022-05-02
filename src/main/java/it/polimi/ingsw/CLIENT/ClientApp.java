package it.polimi.ingsw.CLIENT;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) throws IOException{
        System.out.println("\nWelcome to Eryantis! \n\nStarting the client...");
        NetworkHandler nh = new NetworkHandler();
        LocalModel model = new LocalModel();
        View View = new View(model);
        nh.startClientWithThreads(View, model);
    }
}
