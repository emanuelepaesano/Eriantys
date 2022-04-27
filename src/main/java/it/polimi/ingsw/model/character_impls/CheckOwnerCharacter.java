package it.polimi.ingsw.model.character_impls;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;

import java.util.Scanner;

/**
 * Choose an island to resolve immediately.
 */
public class CheckOwnerCharacter extends Characters {
    int cost;

    public CheckOwnerCharacter() {
        this.cost = 3;
    }

    public void play(Player player, Game game) {
        //choose an island to checkOwner() immediately
        System.out.println(player + ", please choose an island to resolve.");
        Scanner scanner = new Scanner(System.in);
        Island island = player.getEntrance().askWhichIsland(game.getGameMap(), scanner);
        island.checkOwner(game.getTableOrder());
        this.cost += 1;
    }

    public int getCost() {
        return cost;
    }
}
