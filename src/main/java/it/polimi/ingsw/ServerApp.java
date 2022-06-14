package it.polimi.ingsw;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.messages.ActionPhaseMessage.ActionPhaseType.update;
import static it.polimi.ingsw.messages.IslandInfoMessage.IslandInfoType.init;
import static it.polimi.ingsw.messages.IslandInfoMessage.IslandInfoType.updateMap;


/**
 * this will be the main to play the game.
 */
public class ServerApp {
    public static void main(String[] args) throws IOException {

        ClientHandler server = new ClientHandler(1337);
        int numplayers = server.startServer();
        GameController gc = new GameController(numplayers,server.views);
        Game game = gc.getGame();
        new StartGameMessage().send(server.views);
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
        for (Player player: game.getTableOrder()){
            VirtualView pView = gc.getControllerMap().get(player).getPlayerView();
            if (winners.contains(player)) {
                if (winners.size()>1) {
                    List<Player> otherWins = new ArrayList<>(winners);
                    winners.remove(player);
                    new EndGameMessage(otherWins, EndGameMessage.EndGameType.TIE).send(pView);
                }
                else {new EndGameMessage(winners, EndGameMessage.EndGameType.WIN).send(pView);}
            }
            else {
                new EndGameMessage(winners, EndGameMessage.EndGameType.LOSE).send(pView);
            }
        }
//        server.closeEverything();
    }
}
