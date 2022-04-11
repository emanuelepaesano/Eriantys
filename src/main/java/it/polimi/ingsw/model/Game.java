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
        this.currentOrder = startPlayersandOrder(); //give a starting random playerOrder to the game
        this.round = 1;



    }
    public static Game getInstance(){
        if (game == null) {
            game = new Game();
        }
        return game;

    }

    private static List<Player> startPlayersandOrder(){
        /**
         * for as many as numplayers,
         * ask to the players their name and initialize the players.
         * then will make the game.playerorder randomly and return it
         */

        return null;
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

    public static void main(String[] args) {
        Game game = Game.getInstance();
        System.out.println(game.bag.get(StudColor.RED));

    }
}


