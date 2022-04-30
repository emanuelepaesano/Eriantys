package it.polimi.ingsw.CLIENT;


import java.util.Scanner;

public class View {
    NetworkHandler networkHandler;
    //we will read the input from the user and send it
    Scanner scanner = new Scanner(System.in);

    ViewState currentView;


    public View(NetworkHandler nh) {
        this.networkHandler = nh;
    }

    public void update(ViewState newView) {
        currentView = newView;
        currentView.display();
    }

    public String getUserInput(){
        String userInput = scanner.nextLine();
        //might send this to the local controller if we do it
        return userInput;
    }

    public ViewState getCurrentView() {
        return currentView;
    }
}
