package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * This turn player has +2 influence
 */
class MoreInfluenceCharacter extends Characters {

    int cost;
    int maxCost;

    public MoreInfluenceCharacter() {
        this.cost = 1;
        this.maxCost = 2;
    }

    public void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        //this turn +2 influence (n.b. you cant combine characters)
        if (!Characters.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Characters.payandUpdateCost(player,cost,maxCost);
            player.setBaseInfluence(2);

    }

    public void reset(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        player.setBaseInfluence(0);
    }
    public int getCost() {
        return cost;
    }

}
