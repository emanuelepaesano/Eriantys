package it.polimi.ingsw.model;

import java.util.*;

public class Player {
    private int id ;
    private String playerName;
    private TowerColor towerColor;
    private Integer wizard;
    private DiningRoom diningRoom;
    private Integer towers;
    private Map<Assistant, Boolean> assistants;

    public Player(int id) {
        this.id = id;
        this.playerName = askPlayerName();
        this.assistants = buildDeck();
        this.diningRoom = new DiningRoom();
        this.towers = null; //only initialization, the game controller will change it immediately
    }

    private TreeMap<Assistant, Boolean> buildDeck(){
        TreeMap<Assistant,Boolean> tm = new TreeMap<>();
        for (Assistant as : Assistant.values()) {
            tm.put(as, true);
        }
        return tm;
    }


    //we have to show this only to one player at a time
    private String askPlayerName() {
        System.out.println("Player " + this.id + ", enter your nickname:");
        return (new Scanner(System.in).nextLine());
        //create an anonymous scanner from keyboard,
        //ask the user for input and return.
    }



    public int askWizard(ArrayList<Integer> remainingWizards) {
        System.out.println(this.playerName + ", choose your wizard number among these: " + remainingWizards);
        while (true) {
            int input = Integer.parseInt(new Scanner(System.in).nextLine());
            if (remainingWizards.contains(input)){
                Integer wiz = remainingWizards.get(remainingWizards.indexOf(input));
                this.wizard = wiz;
                return wiz;
            }

        }
    }


    /**
     * @param remainingColors the remaining colors, by the game controller
     * @return the TowerColor chosen by the player among the remaining ones
     */
    public TowerColor askTowerColor(ArrayList<TowerColor> remainingColors) {
        System.out.println(this.playerName + ", please choose your tower color among the available ones: " + remainingColors);
        while (true) {
            try {            String input = new Scanner(System.in).nextLine();
                TowerColor choice = TowerColor.valueOf(input.toUpperCase());
                if (remainingColors.contains(choice)){
                    this.towerColor = choice;
                    return choice;
                }
            } catch (IllegalArgumentException ignored) {}

            System.out.println("Not an acceptable color, available colors are: "+ remainingColors.toString());
        }
    }


    /**
     * asks an assistant as input from those remaining, turns it to false in the map and returns it.
     * This way the GameController will then join all the played assistants and choose the new playerOrder
     */
    public Assistant playAssistant(){
        ArrayList<Assistant> remass = new ArrayList<>(); //list of remaining assistants
        for (Assistant key: this.assistants.keySet()){
            if (this.assistants.get(key)){
                remass.add(key);
            }
        }
        while (true) {
            System.out.println(this.playerName + ", play one of your remaining assistants (speed value): " + remass);
            String input = new Scanner(System.in).nextLine();
            try {
                Assistant choice = Assistant.valueOf(input.toUpperCase());
                if (remass.contains(choice)){
                    this.assistants.replace (choice, true, false);
                    return choice;
            }
            } catch (IllegalArgumentException ignored) {} //cosi è eccellente, devo farlo anche nelle altre
            System.out.println("Not a valid assistant, take one from the list: " + remass);
            }
        }





    //GETTERS SETTERS

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public Integer getWizard() {
        return wizard;
    }

    public void setTowers(Integer towers) {
        this.towers = towers;
    }
    public Integer getTowers() {
        return towers;
    }
}
