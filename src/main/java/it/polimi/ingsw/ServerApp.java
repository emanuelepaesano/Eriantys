package it.polimi.ingsw;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.ServerStarter.declareWin;
import static it.polimi.ingsw.messages.ActionPhaseMessage.ActionPhaseType.TEST;
import static it.polimi.ingsw.messages.ActionPhaseMessage.ActionPhaseType.update;
import static it.polimi.ingsw.messages.IslandInfoMessage.IslandInfoType.init;
import static it.polimi.ingsw.messages.IslandInfoMessage.IslandInfoType.updateMap;


/**
 * this will be the main to play the game.
 */
public class ServerApp {

    public static final Object lock = new Object();
    public static void main(String[] args) throws IOException {

        ServerStarter server = new ServerStarter(1337);
        int numplayers = server.startServer();
        GameController gc = new GameController(numplayers,server.views);
        Game game = gc.getGame();
        new StartGameMessage().send(server.views);
        Message info =  new IslandInfoMessage(game, init);
        try {
            for (PlayerController pc : gc.getControllers()) {
                new ActionPhaseMessage(pc.getPlayer(), update).send(pc.getPlayerView());
                //  to update other players' school
                List<Player> otherPlayers = new ArrayList<Player>(game.getTableOrder());
                otherPlayers.remove(pc.getPlayer());
                new SwitcherMessage(otherPlayers).sendAndCheck(pc.getPlayerView());
            }
        }catch (DisconnectedException ex){ServerStarter.stopGame(false);}
        info.send(server.views);
        while (!game.isOver()) {
            gc.doPlanningPhase(game);
            //main game loop. We will skip one player if they are disconnected
            for (Player player : game.getCurrentOrder()) {
                try {
                    PlayerController pc = gc.getControllers().get(game.getTableOrder().indexOf(player));
                    //  to update other players' school
                    List<Player> otherPlayers = new ArrayList<>(game.getTableOrder());
                    otherPlayers.remove(player);
                    new SwitcherMessage(otherPlayers).send(pc.getPlayerView());

                    new IslandInfoMessage(game, updateMap).send(pc.getPlayerView());
                    game.setCurrentPlayer(player);
                    gc.doActions(pc);
                    int nmoves = pc.askMNMoves();
                    game.getGameMap().moveMotherNatureAndCheck(game.getTableOrder(), nmoves);
                    new IslandInfoMessage(game, updateMap).send(pc.getPlayerView());
                    pc.getEntranceController().fillFromClouds(game.getClouds());
                    gc.resetCharacters(game, pc);
                    new IslandInfoMessage(game, updateMap).send(server.views);
                    new ActionPhaseMessage(pc.getPlayer(), update).send(pc.getPlayerView());
                    game.checkGameEndCondition("towerend", player);
                    game.checkGameEndCondition("islandend", player);
                    if (game.isOver()) {
                        break;
                    }
                } catch (DisconnectedException disconnectedView) {
                    System.out.println("Disconnected exception thrown");
                    List<VirtualView> activeViews = server.views.stream().filter(v -> !v.isDisconnected()).toList();
                    while (activeViews.size() == 1) {
                        try {
                            Timer toWin = new Timer(45000, declareWin);
                            toWin.start();
                            synchronized (lock) {
                                lock.wait();
                            }
                            toWin.stop();
                            activeViews = server.views.stream().filter(v -> !v.isDisconnected()).toList();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    continue;
                }
                game.newRoundOrEnd();
            }
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
        ServerStarter.stopGame(true);
    }
}
