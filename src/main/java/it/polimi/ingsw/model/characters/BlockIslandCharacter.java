package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.IslandInfoMessage;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.messages.IslandInfoMessage.IslandInfoType.updateMap;

public class BlockIslandCharacter extends Character {

    int numTiles;

    public BlockIslandCharacter() {
        this.cost = 2;
        this.maxCost = 3;
        this.numTiles = 4;
        this.description = "Place a No Entry tile on an island of your choice. The first time mother nature lands there, " +
                "influence will not be calculated, and the No Entry Tile will be put back on this card.";
        this.number = 12;
    }

    /**
     * Place a No Entry tile on an island of your choice. The first time mother nature lands,
     * do not calculate influence and put the No Entry tile back on this card.
     */
    public boolean play(Game game, PlayerController pc) throws DisconnectedException {
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            Character.sendNoMoneyMessage(pc.getPlayerView());
            return false;
        }
        new NoReplyMessage(false,"Play Character","Pick Island",
                "Pick one island to block.").send(pc.getPlayerView());
        Island island = pc.getEntranceController().askWhichIsland(game.getGameMap());
        island.setBlocked(true);
        numTiles--;
        cost = Character.payandUpdateCost(player,cost,maxCost);
        new IslandInfoMessage(game, updateMap).send(pc.getPlayerView());
        return true;
    }

    public int getNumTiles() {
        return numTiles;
    }

    public void setNumTiles(int numTiles) {
        this.numTiles = numTiles;
    }
}
