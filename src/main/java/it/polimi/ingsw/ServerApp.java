package it.polimi.ingsw;

import it.polimi.ingsw.messages.GenInfoMessage;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.util.List;


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
            for (Player player : game.getCurrentOrder()) {
                PlayerController pc = gc.getControllers().get(game.getTableOrder().indexOf(player));
                game.setCurrentPlayer(player);
                gc.doActions(pc);
                int nmoves = pc.askMNMoves();
                game.getGameMap().moveMotherNatureAndCheck(game.getTableOrder(), nmoves);
                pc.getEntranceController().fillFromClouds(game.getClouds());
                game.checkGameEndCondition("towerend", player);
                game.checkGameEndCondition("islandend", player);
                if (game.isOver()) {
                    break;
                }
            }
            game.newRoundOrEnd();
        }
        List<Player> winners = game.getWinner();
        if (winners.size()>1){
            new StringMessage("Game is a tie!" + winners + "win.").send(server.views);
        }
        else new StringMessage("Game Over!!!" + winners.get(0)+ "wins!");
        server.closeEverything();
    }
}
