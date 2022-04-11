package it.polimi.ingsw.model;
import java.util.*;

/**
 * This is a singleton class.
 * As such the constructor is private and the only instance
 * is built with the getInstance method
 */
public class Game {
    private static Game game = null;
    private Integer numPlayers;
//    private List<Island> islandMap; //questa forse meglio con classe arcipelago
    private Player currentPlayer;
    private List<Player> currentOrder;
    private Integer round;
    private Map<StudColor, Integer> bag;


    private Game() {
        /**
         * initialize bag and random order of player. round starts from 1
         */
        this.bag = buildBag();

        this.round = 1;



    }
    public static Game getInstance(){
        if (game == null) {
            game = new Game();
        }
        return game;

    }

    private static HashMap<StudColor, Integer> buildBag(){
        HashMap bag = new HashMap<StudColor,Integer>();
        bag.put(StudColor.BLUE, 26);
        bag.put(StudColor.YELLOW, 26);
        bag.put(StudColor.RED, 26);
        bag.put(StudColor.GREEN, 26);
        bag.put(StudColor.PINK, 26);

        return bag;

    }




    //BELOW THIS ALL GETTER AND SETTERS
    public void setCurrentOrder(List<Player> currentOrder) {
        this.currentOrder = currentOrder;
    }
    public List<Player> getCurrentOrder() {
        return currentOrder;
    }
}


