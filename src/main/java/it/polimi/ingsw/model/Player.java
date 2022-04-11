package it.polimi.ingsw.model;

import java.util.*;

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
        //this.towerColor = askTowerColor(ArrayList<>);
        // TODO: 11/04/2022  this.school = new School();
       // this.towers = 6 or 8 depending on playernumber
    }

    public int askWizard(ArrayList remainingWizards) {
        System.out.println(this.playerName + ", please choose your wizard number among these: " + remainingWizards);
        while (true) {
            int input = Integer.parseInt(new Scanner(System.in).nextLine());
            if (remainingWizards.contains(input)){
                Object wiz = remainingWizards.get(remainingWizards.indexOf(input));
                this.wizard = (Integer) wiz;
                return (Integer) wiz;
            }

        }
    }

    private String askPlayerName() {
        System.out.println("enter your player name:");
        return (new Scanner(System.in).nextLine());
        //create an anonymous scanner from keyboard,
        //ask the user for input and return
    }

    /**
     * @param remainingColors
     * @return the TowerColor chosen by the player among the remaining ones
     */
    public TowerColor askTowerColor(ArrayList<TowerColor> remainingColors) {
        System.out.println(this.playerName + ", please choose your tower color among the available ones: " + remainingColors.toString());
        ArrayList<String> remColsString = new ArrayList<>();
        for (TowerColor tc : remainingColors ){
             remColsString.add(tc.asString());
        }
        while (true) {
            String input = new Scanner(System.in).nextLine();
            String word = input.toUpperCase();
            if (remColsString.contains(word)){
                TowerColor c = remainingColors.get(remColsString.indexOf(word));
                this.towerColor = c;
                return c;
            }
            else{
                System.out.println("Not an acceptable color, available colors are: "+ remainingColors.toString());
            }
        }
    }




    //GETTERS SETTERS

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public Integer getWizard() {
        return wizard;
    }
}
