package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

/**
 * For this turn, player has +2 Mother Nature movements
 */
class MoreMovementsCharacter extends Character {

    public MoreMovementsCharacter( ) {
        this.cost = 1;
        this.maxCost = 2;
        description="You may move Mother Nature up to 2 additional islands than is indicated" +
                "by your Assistant.";
        this.number = 4;
        students = new ArrayList<>();

    }

    public synchronized void play(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Character.payandUpdateCost(player,cost,maxCost);

        player.setBaseMoves(2);


    }

    public void reset(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        player.setBaseMoves(0);
    }

}
