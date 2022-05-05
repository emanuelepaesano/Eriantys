package it.polimi.ingsw;

import it.polimi.ingsw.messages.GenInfoMessage;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.IOException;


/**
 * this will be the main to play the game.
 * it creates the game controller to start. For now only a test
 */
public class ServerApp {
    public static void main(String[] args) throws IOException {

        ServerHandler server = new ServerHandler(1337);
        int numplayers = server.startServer();
        GameController gc = new GameController(numplayers,server.views);
        Game game = gc.getGame();
        Message info =  new GenInfoMessage(game);
        info.send(server.views);
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
        server.closeEverything();
    }
}
