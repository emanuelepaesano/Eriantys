package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColor;

import java.util.*;

public class GameController {
    private Game game;
    /**
     * In the constructor we can put the methods to inizialize the game.
     * Our main will call the constructor of this class to start the game
     */
    public GameController() {
        int numplayers = AskForPN();
        Game game = Game.getInstance(numplayers); //initializes the game, because it's the first call
        this.game = Game.getInstance(numplayers);
        // TODO: 12/04/2022 might consider moving some of these to a PlayerController,
        //  since we need to initialize also a diningroom and entrance for every player
        List<Player> startingOrder = startPlayersandOrder(numplayers, game); //give a starting random playerOrder to the game
        game.setCurrentOrder(startingOrder);
        //we also need a random table_order to determine what going "clockwise" means
        askAllForTC(game);
        askAllForWiz(game);

        // TODO: 11/04/2022 initialize the view(s)



    }

    /**
     *
     * @return Asks and returns the number of players for this game
     */
    private int AskForPN() {
        int input = 0;
        while ((input != 3) && (input != 2)) {
            System.out.println("Enter number of players:");
            input = Integer.parseInt(new Scanner(System.in).nextLine());
        }
        return input;
    }

    /**
     *
     * @param numplayers player number from askForPN
     * @return initializes the players (and their tower number), returns a random starting order.
     * This one may be moved to a playerController, or maybe from this we make the playercontroller
     * instead
     */
    private static List<Player> startPlayersandOrder(Integer numplayers, Game game){
        ArrayList<Player> startingOrder = new ArrayList<>();
        for (int i=0; i<numplayers; i++){
            PlayerController pc = new PlayerController(i+1, game);
            pc.player.setTowers(numplayers==3 ? 6 : 8);
            startingOrder.add(pc.player);
        }
        Collections.shuffle(startingOrder);
        return startingOrder;
    }



    /**
     * Cycles through players and asks them a color.
     * It will be stored as an attribute of the player
     */
    private static void askAllForTC(Game game){
        ArrayList<TowerColor> remainingColors = new ArrayList<>(Arrays.asList(TowerColor.values()));
        for (Player player : game.getCurrentOrder()){
            TowerColor c = player.askTowerColor(remainingColors);
            remainingColors.remove(c);
        }
    }

    /**
     * Cycles through players and asks them a wizard number.
     * It will be stored as an attribute of the player
     */
    public static void askAllForWiz(Game game) {
        ArrayList<Integer> remainingWizards = new ArrayList<>(Arrays.asList(1,2,3,4));
        for (Player player : game.getCurrentOrder()){
            Integer wiz = player.askWizard(remainingWizards);
            remainingWizards.remove(wiz);
        }
    }

    public static void main(String[] args) {
        // Test for the game controller
        GameController gc = new GameController();
        for (Player p : gc.game.getCurrentOrder()){
            System.out.println(p.getPlayerName() +" has this in the entrance:" + p.getEntrance().toString()
            + ";\n these in the tables:" + p.getDiningRoom().getTables());
        }


    }
}
