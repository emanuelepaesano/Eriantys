package it.polimi.ingsw.model;

import java.util.List;
import java.util.Scanner;

public class Player {
    private Integer id ;
    private String playerName;
    private TowerColor towerColor;
    private Integer wizard;
    private School school;
    private Integer towers;
    private List<Assistant> assistants;

    public Player() {
        this.playerName = askPlayerName();
        this.towerColor = askTowerColor();
        this.wizard = askWizard();
        // TODO: 11/04/2022  this.school = new School();
       // this.towers = 6 or 8 depending on playernumber
    }

    private Integer askWizard() {
        return 1;
        /**
         * anche questo dovra andare per iterated dictatorship
         */
    }

    private String askPlayerName() {
        System.out.println("enter your player name:");
        return (new Scanner(System.in).nextLine());
        //create an anonymous scanner from keyboard,
        //ask the user for input and return
    }

    private TowerColor askTowerColor() {
        /**
         * dobbiamo fare in modo che nessuno possa avere lo stesso towercolor
         * (serve una lista di towercolors nel game?)
         */
        return null;
    }

}
