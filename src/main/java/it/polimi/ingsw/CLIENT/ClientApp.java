package it.polimi.ingsw.CLIENT;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) throws IOException{
        System.out.println("\nWelcome to Eryantis! \n\nStarting the client...");
        UserView View = new UserView();
        NetworkHandler nh = new NetworkHandler(View);
        nh.startListenerThread();
    }
}
