package it.polimi.ingsw.model.characters;

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

    public void play(Player player, Game game) {
        //choose an island to checkOwner() immediately
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        System.out.println(player + ", please choose an island to resolve.");
        Scanner scanner = new Scanner(System.in);
        Island island = game.getGameMap().askWhichIsland(scanner);
        island.checkOwner(game.getTableOrder());
        this.cost = Character.payandUpdateCost(player,cost);
    }

    public int getCost() {
        return cost;
    }
}
