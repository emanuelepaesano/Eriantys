package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * For this turn, player has +2 Mother Nature movements
 */
class MoreMovementsCharacter extends Characters {
    int cost;
    int maxCost;


    public MoreMovementsCharacter( ) {
        this.cost = 1;
        this.maxCost = 2;

    }

    public synchronized void play(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        if (!Characters.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Characters.payandUpdateCost(player,cost,maxCost);

        player.setBaseMoves(2);


    }

    public void reset(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        player.setBaseMoves(0);
    }

    public int getCost() {
        return cost;
    }
}
