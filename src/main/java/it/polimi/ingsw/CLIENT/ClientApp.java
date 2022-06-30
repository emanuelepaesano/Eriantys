package it.polimi.ingsw.CLIENT;

public class ClientApp {
    private static NetworkHandler nh;

    public static NetworkHandler getNh() {
        return nh;
    }

    public static void main(String[] args){
        System.out.println("\nWelcome to Eryantis! \n\nStarting the client...");
        UIManager.main(args);
    }
}
