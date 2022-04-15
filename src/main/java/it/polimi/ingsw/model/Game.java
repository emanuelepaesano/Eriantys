package it.polimi.ingsw.model;
import java.util.*;


public class Game {
    private static Game game = null;
    private int numPlayers;
    private Player currentPlayer;
    private final List<Player> tableOrder;
    private List<Player> currentOrder;
    private Integer round;
    private Map<StudColor, Integer> bag;
    private List<Cloud> clouds;
    private GameMap gameMap;

    /**
     * initialize bag and random order of player. round starts from 1
     */
    public Game(int numPlayers, List<Player> startingOrder) {
        bag = buildBag();
        round = 1;
        this.numPlayers = numPlayers;
        currentOrder= startingOrder;
        tableOrder = startingOrder;
        gameMap = new GameMap(); //this will start the islands, motherNature, initial students


    }


    private static HashMap<StudColor,Integer> buildBag(){
        HashMap<StudColor,Integer> bag = new HashMap<>();
        bag.put(StudColor.BLUE, 24);
        bag.put(StudColor.YELLOW, 24);
        bag.put(StudColor.RED, 24);
        bag.put(StudColor.GREEN, 24);
        bag.put(StudColor.PINK, 24);
        return bag;

    }




    //BELOW THIS ALL GETTER AND SETTERS

    public void setCurrentOrder(List<Player> currentOrder) {
        this.currentOrder = currentOrder;
    }


    public List<Player> getCurrentOrder() {
        return currentOrder;
    }
    public Integer getNumPlayers() {
        return numPlayers;
    }

    public List<Player> getTableOrder() {
        return tableOrder;
    }
}


