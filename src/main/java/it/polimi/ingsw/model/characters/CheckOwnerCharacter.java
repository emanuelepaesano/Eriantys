package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * Choose an island to resolve immediately.
 */
class CheckOwnerCharacter extends Characters {
    int cost;
    int maxCost;

    public CheckOwnerCharacter() {
        this.cost = 3;
        this.maxCost = 4;
    }

    public void play(Game game, PlayerController pc) {
        //choose an island to checkOwner() immediately
        Player player = pc.getPlayer();
        if (!Characters.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        System.out.println(player + ", please choose an island to resolve.");
        Island island = pc.getEntranceController().askWhichIsland(game.getGameMap());
        island.checkOwner(game.getTableOrder());
        this.cost = Characters.payandUpdateCost(player,cost, maxCost);
    }

    public int getCost() {
        return cost;
    }
}
