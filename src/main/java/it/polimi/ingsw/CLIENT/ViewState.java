package it.polimi.ingsw.CLIENT;

import java.io.Serializable;
import java.util.Scanner;

public interface ViewState extends Serializable {

    Scanner scanner = new Scanner (System.in);


    void display();

}
