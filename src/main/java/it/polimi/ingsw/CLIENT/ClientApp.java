package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.CLIENT.ViewImpls.CLIView;

import java.io.IOException;

public class ClientApp {
    private static NetworkHandler nh;

    public static NetworkHandler getNh() {
        return nh;
    }

    public static void main(String[] args){
        System.out.println("\nWelcome to Eryantis! \n\nStarting the client...");
        NetworkHandler.main(args);

    }
}
