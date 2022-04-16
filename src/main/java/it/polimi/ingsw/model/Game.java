package it.polimi.ingsw.model;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PlayerController;

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
    public Game(int numPlayers) {
        round = 1;
        this.numPlayers = numPlayers;
        currentOrder= startPlayersandOrder(numPlayers);
        tableOrder = new ArrayList<>(currentOrder); //this is to make a copy
        gameMap = new GameMap(this); //this will start the islands, motherNature, initial students
        bag = makeBag();
        clouds = makeClouds(numPlayers);
        currentPlayer = currentOrder.get(0);

    }


    /**
     * @return initializes the players (and their tower number), returns a random starting order.
     * This one may be moved to a playerController, or maybe from this we make the playercontroller
     * instead
     */
    public List<Player> startPlayersandOrder(int numplayers){
        ArrayList<Player> startingOrder = new ArrayList<>();
        for (int i=0; i< numplayers; i++){
            PlayerController pc = new PlayerController(i+1, this);
            startingOrder.add(pc.getPlayer());
        }
        Collections.shuffle(startingOrder);
        return startingOrder;
    }



    private HashMap<StudColor,Integer> makeBag(){
        HashMap<StudColor,Integer> bag = new HashMap<>();
        for (StudColor sc : StudColor.values()){
            bag.put(sc,24);
        }
        return bag;
    }

    /**
     *
     * @param numPlayers Make as many clouds as there are players.
     *                   They are initialized with 0 students.
     */
    private List<Cloud> makeClouds(int numPlayers){
        List<Cloud> clouds = new ArrayList<>();
        for (int i=0; i<numPlayers; i++){
            clouds.add(new Cloud());
        }
        return clouds;
    }

    public static void main(String[] args) {
      //  List<Player> so = GameController.startPlayersandOrder(3);
        Game g = new Game(3);
        System.out.println(g.clouds);
        System.out.println(g.bag);
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

    public List<Cloud> getClouds() {
        return clouds;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}


