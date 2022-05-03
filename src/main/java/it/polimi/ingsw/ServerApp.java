package it.polimi.ingsw;

import it.polimi.ingsw.CLIENT.GeneralViewState;
import it.polimi.ingsw.CLIENT.ViewState;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.IOException;


/**
 * this will be the main to play the game.
 * it creates the game controller to start. For now only a test
 */
public class ServerApp {
    public static void main(String[] args) throws IOException {
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

        ServerHandler server = new ServerHandler(1337);
        int numplayers = server.startServer();
        GameController gc = new GameController(numplayers,server.views);
        Game game = gc.getGame();
        ViewState send =  new GeneralViewState(game);
        server.updateAllViews(send);
        while (!game.isOver()) {
            gc.doPlanningPhase(game);
            for (PlayerController playerturn: gc.getControllers()) {
                Player player = playerturn.getPlayer();
                game.setCurrentPlayer(player);
                playerturn.doActions(game);
                int nmoves = playerturn.askMNMoves();
                game.getGameMap().moveMotherNatureAndCheck(playerturn.getPlayer(), game.getTableOrder(),nmoves);
                game.checkGameEndCondition("towerend",player);
                game.checkGameEndCondition("islandend",player);
            }
            game.newRoundOrEnd();
        }
        server.closeAll();


    }
}
