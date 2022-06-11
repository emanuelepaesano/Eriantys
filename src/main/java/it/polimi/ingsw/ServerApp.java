package it.polimi.ingsw;

import it.polimi.ingsw.messages.ActionPhaseMessage;
import it.polimi.ingsw.messages.IslandInfoMessage;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.messages.ActionPhaseMessage.ActionPhaseType.update;
import static it.polimi.ingsw.messages.IslandInfoMessage.IslandInfoType.init;
import static it.polimi.ingsw.messages.IslandInfoMessage.IslandInfoType.updateMap;


/**
 * this will be the main to play the game.
 * it creates the game controller to start. For now only a test
 */
public class ServerApp {
    public static void main(String[] args) throws IOException {

        ClientHandler server = new ClientHandler(1337);
        int numplayers = server.startServer();
        GameController gc = new GameController(numplayers,server.views);
        Game game = gc.getGame();
        Message info =  new IslandInfoMessage(game, init);
        for (PlayerController pc: gc.getControllers()){
            new ActionPhaseMessage(pc.getPlayer(), update).send(pc.getPlayerView());
        }
        info.send(server.views);
        while (!game.isOver()) {
            gc.doPlanningPhase(game);
            for (Player player : game.getCurrentOrder()) {
                PlayerController pc = gc.getControllers().get(game.getTableOrder().indexOf(player));
                new IslandInfoMessage(game, updateMap).send(pc.getPlayerView());
                game.setCurrentPlayer(player);
                gc.doActions(pc);
                int nmoves = pc.askMNMoves();
                game.getGameMap().moveMotherNatureAndCheck(game.getTableOrder(), nmoves);
                new IslandInfoMessage(game, updateMap).send(pc.getPlayerView());
                pc.getEntranceController().fillFromClouds(game.getClouds());
                new IslandInfoMessage(game, updateMap).send(server.views);
                new ActionPhaseMessage(pc.getPlayer(),update).send(pc.getPlayerView());
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
