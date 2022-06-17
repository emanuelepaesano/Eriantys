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

    public Island getChosenIsland() {
        return chosenIsland;
    }
    public void setChosenIsland(Island chosenIsland) {
        this.chosenIsland = chosenIsland;
    }
    Island chosenIsland;

    public CheckOwnerCharacter() {
        this.cost = 3;
        this.maxCost = 4;
    }

    private void setUp(PlayerController pc, Game game){
        Island island = pc.getEntranceController().askWhichIsland(game.getGameMap());
        chosenIsland = island;
    }

    public void play(Game game, PlayerController pc) {
        //choose an island to checkOwner() immediately
        Player player = pc.getPlayer();
        if (!Characters.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        System.out.println(player + ", please choose an island to resolve.");
        if (chosenIsland == null){
            setUp(pc, game);
            if(chosenIsland == null){
                return;
            }
        }
        chosenIsland.checkOwner(game.getTableOrder());
        this.cost = Characters.payandUpdateCost(player,cost, maxCost);
    }

    public int getCost() {
        return cost;
    }
}
