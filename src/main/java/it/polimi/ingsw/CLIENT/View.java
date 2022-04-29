package it.polimi.ingsw.CLIENT;


import java.util.Scanner;

public class View {
    NetworkHandler networkHandler;
    //we will read the input from the user and send it
    Scanner scanner = new Scanner(System.in);


    public View(NetworkHandler nh) {
        this.networkHandler = nh;
    }

    public void update() {
        System.out.println(networkHandler.model);
    }

    public String getUserInput(){
        String userInput = scanner.nextLine();
        //might send this to the local controller if we do it
        return userInput;
    }
}
