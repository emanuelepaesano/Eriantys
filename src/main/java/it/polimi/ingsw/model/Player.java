package it.polimi.ingsw.model;

import java.util.*;

public class Player {
    private final int id ;
    private final String playerName;
    private TowerColor towerColor;
    private Integer wizard;
    private DiningRoom diningRoom;
    private Entrance entrance;
    private Integer numTowers;
    private Map<Assistant, Boolean> assistants;
    private Assistant currentAssistant;
    private int availableMoves;

    public Player(int id, DiningRoom diningRoom, Entrance entrance) {
        this.id = id;
        this.diningRoom = diningRoom;
        this.entrance = entrance;
        playerName = askPlayerName();
        assistants = buildDeck();


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
        System.out.println(this.playerName + ", play one of your remaining assistants (speed value): " + remass);
        while (true) {
            String input = new Scanner(System.in).nextLine();
            // TODO: 15/04/2022 would be nice if also putting es.9 or 10 worked
            try {
                Assistant choice = Assistant.valueOf(input.toUpperCase());
                if (remass.contains(choice)){
                    this.currentAssistant = choice;
                    System.out.println("Current assistant for "+ playerName + ": " + choice);
                    this.assistants.replace (choice, true, false);
                    return choice;
                }
            } catch (IllegalArgumentException exception) {System.out.println("Not a valid assistant, take one from the list: " + remass);}
        }
    }

    public int askMNMoves(){
        //this needs to be called at the end of every player's action phase.
        //it allows moving mother nature of: min 1 step, max currentassistant.moves steps.
        //obviously we need to change the bools in the 2 islands.
        //so, we need to see the map, so we call this from the game, or we have to import the game in the constructor
        //only for this which i dont like. Sooo from this we only ask basically.
        Scanner scanner = new Scanner(System.in);
        System.out.println(playerName + ", how many steps do you want to move Mother Nature? " +
                "(At least 1, maximum " + currentAssistant.getMoves() + ")");
        while (true) {
            try {
                int choice = scanner.nextInt();
                if (choice >=1 && choice<= currentAssistant.getMoves()){
                    return choice;
                }
                else{System.out.println("That choice is not allowed! Try again");}
            } catch (IllegalArgumentException ex) {System.out.println("Not a valid number, try again");}
        }
    }



    @Override
    public String toString() {
        return "Player" + id +
                ": \"" + playerName +"\"";
    }

    //GETTERS SETTERS

    public void setNumTowers(Integer numTowers) {
        this.numTowers = numTowers;
    }

    public Map<Assistant, Boolean> getAssistants() {
        return assistants;
    }

    public String getPlayerName() {
        return playerName;
    }

    public DiningRoom getDiningRoom() {
        return diningRoom;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    public Integer getNumTowers() {
        return numTowers;
    }
}
