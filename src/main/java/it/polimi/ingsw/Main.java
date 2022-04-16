package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * this will be the main to play the game.
 * it creates the game controller to start. For now only a test
 */
public class Main {
    public static void main(String[] args) {
        // Test for the game controller
        GameController gc = new GameController();
        Game game = gc.getGame();
        gc.doPlanningPhase(gc.getGame());

        for (Player p : game.getCurrentOrder()){
            System.out.println(p.getPlayerName() +" has this in the entrance:" + p.getEntrance()
            + ";\n these in the tables:" + p.getDiningRoom().getTables());
            System.out.println("tower number: " + p.getNumTowers());
        }



    }
}
