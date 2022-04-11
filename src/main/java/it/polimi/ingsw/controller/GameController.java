package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColor;

import java.net.Inet4Address;
import java.util.*;

public class GameController {
    /**
     * in the constructor we can put the methods to inizialize the game.
     * our main will call the constructor of this to start the game
     */

    public GameController() {
        Integer numplayers = AskForGeneralSettings();
        Game game = Game.getInstance(); //builds the game, because it's the first call
        List<Player> startingOrder = startPlayersandOrder(numplayers); //give a starting random playerOrder to the game
        game.setCurrentOrder(startingOrder);
        askAllForTC(game);
        askAllForWiz(game);

        // initialize the views


    }

    private int AskForGeneralSettings() {
        Integer input = 0;
        while ((input != 3) && (input != 2)) {
            System.out.println("Enter Number of Players:");
            input = Integer.parseInt(new Scanner(System.in).nextLine());
        }
        return input;
    }

    /**
     *
     * @param numplayers
     * @return initializes the players and returns a random starting order
     */
    private static List<Player> startPlayersandOrder(Integer numplayers){
        ArrayList<Player> startingOrder = new ArrayList<>();
        for (int i=0; i<numplayers; i++){
            Player player = new Player();
            startingOrder.add(player);
        }
        Collections.shuffle(startingOrder);
        return startingOrder;
    }
    private static void askAllForTC(Game game){
        ArrayList<TowerColor> remainingColors = new ArrayList<>(Arrays.asList(TowerColor.values()));
        for (Player player : game.getCurrentOrder()){
            TowerColor c = player.askTowerColor(remainingColors);
            remainingColors.remove(c);
        }
        return;
    }

    public static void askAllForWiz(Game game) {
        ArrayList remainingWizards = new ArrayList(Arrays.asList(1,2,3,4));
        for (Player player : game.getCurrentOrder()){
            Integer wiz = player.askWizard(remainingWizards);
            remainingWizards.remove(wiz);
        }
        return;
    }

    public static void main(String[] args) {
        // Test for the game controller
        GameController gc = new GameController();


    }
}
