package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.List;

public class BlockIslandCharacter extends Character {

    int numTiles;

    public BlockIslandCharacter() {
        this.cost = 2;
        this.maxCost = 3;
        this.numTiles = 4;
    }

    /**
     * Place a No Entry tile on an island of your choice. The first time mother nature lands,
     * do not calculate influence and put the No Entry tile back on this card.
     */
    public void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            System.out.println("you don't have enough money!");
            return;
        }
        Island island = pc.getEntranceController().askWhichIsland(game.getGameMap());
        island.setBlocked(true);
        numTiles--;
        Character.payandUpdateCost(player,cost,maxCost);
    }

    public int getNumTiles() {
        return numTiles;
    }

    public void setNumTiles(int numTiles) {
        this.numTiles = numTiles;
    }
}
