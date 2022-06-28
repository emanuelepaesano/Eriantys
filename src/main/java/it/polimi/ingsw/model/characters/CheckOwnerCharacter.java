package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;

/**
 * Choose an island to resolve immediately.
 */
class CheckOwnerCharacter extends Character {

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
        description = "Choose an Island to resolve immediately. \n" +
                "You can still move Mother Nature as usual.";
        this.number = 1;
    }

    private void setUp(PlayerController pc, Game game){
        Island island = pc.getEntranceController().askWhichIsland(game.getGameMap());
        chosenIsland = island;
    }

    public boolean play(Game game, PlayerController pc) {
        //choose an island to checkOwner() immediately
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return false;}
        System.out.println(player + ", please choose an island to resolve.");
        if (chosenIsland == null){
            setUp(pc, game);
            if(chosenIsland == null){
                return false;
            }
        }
        chosenIsland.checkOwner(game.getTableOrder());
        this.cost = Character.payandUpdateCost(player,cost, maxCost);
        return true;
    }

}
