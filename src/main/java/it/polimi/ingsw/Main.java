package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.stream.Stream;


/**
 * this will be the main to play the game.
 * it creates the game controller to start. For now only a test
 */
public class Main {
    public static void main(String[] args) {
      //   Test for the game
//        GameController gc = new GameController();
//        Game game = gc.getGame();
//        gc.doPlanningPhase(gc.getGame());
//
//        game.setCurrentPlayer(game.getCurrentOrder().get(0)); //we don't have a rule for switching between players yet
//        Player testpl = game.getCurrentPlayer();
//        testpl.doActions(game.getGameMap(),game.getTableOrder());
//        System.out.println("Test player entrance after moves: " + testpl.getEntrance() +"\n" +
//                "Islands after player's moves:\n" + game.getGameMap());
//        game.getGameMap().moveMotherNatureAndCheck(testpl, game.getTableOrder());

        Game game = Game.makeGame(3);
        game.doSetUp(false);
        List<Island> islands = game.getGameMap().getArchipelago();
        islands.get(2).setSize(3);
        islands.get(6).setSize(2);
        islands.get(10).setSize(3);
        List<Integer> oldsizes = islands.stream().map(Island::getSize).toList();
        islands.replaceAll(island -> {island.setSize(0); return island;});
        islands.replaceAll(island -> {island.setSize(oldsizes.get(islands.indexOf(island))); return island;});
    }
}
