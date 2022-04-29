package it.polimi.ingsw.CLIENT;


import java.util.Scanner;

public class View {
    //we will read the input from the user and send it
    Scanner scanner = new Scanner(System.in);


    public View() {

    }

    public void update(NetworkHandler nh) {
        System.out.println(nh.model);
    }
}
