package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class GameController {
    /**
     * in the constructor we can put the methods to inizialize the game.
     * our main will call the constructor of this to start the game
     */

    public GameController() {
        Game game = Game.getInstance(); //builds the game, because it's the first call

        // initialize the views


    }


}
