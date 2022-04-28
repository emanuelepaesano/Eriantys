package it.polimi.ingsw.model;

import java.util.*;

public class Player {
    public final int id ;
    private final String playerName;
    private TowerColor towerColor;
    private Integer wizard;
    private final DiningRoom diningRoom;
    private final Entrance entrance;
    private Integer numTowers;
    private Map<Assistant, Boolean> assistants;
    private Assistant currentAssistant;
    private final int numActions;
    private int baseMoves = 0;
    private int baseInfluence = 0;
    private boolean orEqual = false;

    public static Player makePlayer(int id, int numPlayers){
        String pName = askPlayerName(id);
        Map<Assistant, Boolean> assistants = buildDeck();
        DiningRoom diningRoom = new DiningRoom();
        Entrance entrance = new Entrance(numPlayers);
        return new Player(id, pName, assistants, (numPlayers==3? 4 : 3), (numPlayers == 3? 6 : 8), diningRoom, entrance);
    }

    private static String askPlayerName(int id)  {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player " + id + ", enter your nickname:");
        while (true){
            String name = scanner.nextLine();
            if (name.length() < 20){
                return name;
            }
            System.out.println("name too long! Insert a shorter name");
        }
    }


    private static Map<Assistant, Boolean> buildDeck(){
        Map<Assistant,Boolean> tm = new TreeMap<>();
        for (Assistant as : Assistant.values()) {
            tm.put(as, true);
        }
        return tm;
    }


    private Player(int id, String name, Map<Assistant, Boolean> deck,
                  int numActions, int numTowers, DiningRoom diningRoom, Entrance entrance) {
        this.id = id;
        playerName = name;
        assistants = deck;
        this.numActions = numActions;
        this.numTowers = numTowers;
        this.diningRoom = diningRoom;
        this.entrance = entrance;
    }



    /**
     *
     * @param remainingWizards the remaining wizards, by askAllforWiz()
     * @return the wizard chosen by the player
     */
    public int askWizard(List<Integer> remainingWizards) {
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
    public TowerColor askTowerColor(List<TowerColor> remainingColors) {
        System.out.println(this.playerName + ", please choose your tower color among the available ones: " + remainingColors);
        while (true) {
            try {            String input = new Scanner(System.in).nextLine();
                TowerColor choice = TowerColor.valueOf(input.toUpperCase());
                if (remainingColors.contains(choice)){
                    this.towerColor = choice;
                    return choice;
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Try again!");
            }
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


    // TODO: 16/04/2022 idk where to put this
    /**
     * This is the main method for the action phase of each player. It asks the player which action they want to do
     * and then performs the action, until they used all of their moves.
     */
    public void doActions(GameMap gm, List<Player> players){
        int availableActions = numActions;
        //this will be an actionlistener linked to 2 buttons. depending on the button pressed
        //(movetodiningroom or movetoisland) the controller calls a different method, then updates model
        while (availableActions>0) {
            String action = askWhichAction(availableActions); //this will come from the view, so it must be in a controller
            if (Objects.equals(action, "diningroom")) {
                availableActions -= entrance.moveToDiningRoom(availableActions, this.diningRoom);
                this.diningRoom.checkProfessors(players,orEqual);
            }
            else if (Objects.equals(action, "islands")){
                availableActions -= entrance.moveToIsland(availableActions, gm);}
        }
        System.out.println("After your moves: " + this.diningRoom);
    }

    private String askWhichAction(int availableActions){
        Scanner scanner = new Scanner(System.in);
        System.out.printf("%s, where do you want to move your students (%d moves left)? Please type \"islands\" or \"diningroom\" "
                , playerName,availableActions);
        return scanner.nextLine();
    }

    /**
     *
     * @return The number of steps the player wants to move mother Nature. This method is now only called from GameMap.moveMotherNature().
     * This could change if we choose to move that method
     */
    public int askMNMoves(){
        int possibleMoves = baseMoves + currentAssistant.getMoves();
        Scanner scanner = new Scanner(System.in);
        System.out.println(playerName + ", how many steps do you want to move Mother Nature? " +
                "(At least 1, maximum " + possibleMoves + ")");
        while (true) {
            try {
                int choice = scanner.nextInt();
                if (choice >=1 && choice<= possibleMoves){
                    return choice;
                }
                else{System.out.println("That choice is not allowed! Try again");}
            } catch (IllegalArgumentException ex) {System.out.println("Not a valid number, try again");}
        }
    }

    /**
     *Method to calculate this player's influence on an island. It gets called by Island.checkOwner().
     */
    public int calculateInfluence(Island island) {
        int influence = baseInfluence;
        if (island.getOwner() == this) {influence += island.size;}
        for (Student student : Student.values()){
            if (this.diningRoom.getProfessors().get(student)){
                influence += island.getStudents().get(student);
            }
        }
        return influence;
    }




    //method to check winning condition: return 1 if we have 0 towers
    public Boolean checkNumTowers(){
        return (numTowers == 0);
    }

    @Override
    public String toString() {
        return "Player" + id +
                ": \"" + playerName +"\"";
    }




    //GETTERS SETTERS

    public int getCoins(){
        return this.getDiningRoom().getCoins();
    }
    public void setCoins(int coins){
        this.getDiningRoom().setCoins(coins);
    }
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

    public Assistant getCurrentAssistant() {
        return currentAssistant;
    }

    public Integer getWizard() {
        return wizard;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void setCurrentAssistant(Assistant currentAssistant) {
        this.currentAssistant = currentAssistant;
    }

    public int getBaseInfluence() { return baseInfluence;}

    public void setBaseInfluence(int baseInfluence) { this.baseInfluence = baseInfluence;}

    public int getBaseMoves() {
        return baseMoves;
    }

    public void setBaseMoves(int baseMoves) {
        this.baseMoves = baseMoves;
    }

    public void setOrEqual(boolean orEqual) {this.orEqual = orEqual;}
}
