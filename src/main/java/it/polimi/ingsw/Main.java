package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.*;
import java.util.stream.Stream;


/**
 * this will be the main to play the game.
 * it creates the game controller to start. For now only a test
 */
public class Main {
    public static void main(String[] args) {
         //Test for the game
        GameController gc = new GameController();
        Game game = gc.getGame();
        gc.doPlanningPhase(gc.getGame());

        game.setCurrentPlayer(game.getCurrentOrder().get(0)); //we don't have a rule for switching between players yet
        Player testpl = game.getCurrentPlayer();
        testpl.doActions(game.getGameMap(), game.getTableOrder());
        System.out.println("Test player entrance after moves: " + testpl.getEntrance() +"\n" +
                "Islands after test player's moves:\n" + game.getGameMap());
        game.getGameMap().moveMotherNatureAndCheck(game.getCurrentPlayer(),game.getTableOrder());

    }
}
