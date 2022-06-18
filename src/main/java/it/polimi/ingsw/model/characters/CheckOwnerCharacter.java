package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;

import java.util.Scanner;

/**
 * Choose an island to resolve immediately.
 */
class CheckOwnerCharacter extends Characters {
    int cost;

    public CheckOwnerCharacter() {
        this.cost = 3;
    }

    // TODO: 18/06/2022 We cannot keep the try-catch like this, but idk what was done with characters
    public void play(Game game, PlayerController pc) {
        //choose an island to checkOwner() immediately
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        System.out.println(player + ", please choose an island to resolve.");
        Island island = null;
        try {
            island = pc.getEntranceController().askWhichIsland(game.getGameMap());
        } catch (DisconnectedException ex){}
        island.checkOwner(game.getTableOrder());
        this.cost = Character.payandUpdateCost(player,cost);
    }

    public int getCost() {
        return cost;
    }
}
