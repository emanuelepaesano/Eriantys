package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * For this turn, >= instead of > in checkProfessors()
 */
class CheckProfCharacter extends Characters {
    int cost;
    int maxCost;


    public CheckProfCharacter() {
        this.cost = 2;
        this.maxCost = 3;


    }

    public synchronized void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        if (!Characters.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Characters.payandUpdateCost(player,cost, maxCost);

        player.setOrEqual(true);


    }

    public void reset (Game game, PlayerController pc){
        Player player = pc.getPlayer();
        player.setOrEqual(false);
    }
    public int getCost() {
        return cost;
    }
}
