package it.polimi.ingsw.CLIENT;


import java.util.Scanner;

public class View {
    //we will read the input from the user and send it
    Scanner scanner = new Scanner(System.in);
    LocalModel model;
    ViewState currentView;


    public View(LocalModel model) {
        this.model = model;
    }

    public void update() {
        currentView = model.getState();
        currentView.display();
    }

    public void getMessage() {
        System.out.println(model.getMessage());
    }

    public String getUserInput() {
        String userInput = scanner.nextLine();
        //might send this to the local controller if we do it
        return userInput;
    }
}

