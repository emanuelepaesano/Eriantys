package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

/**
 * For this turn, >= instead of > in checkProfessors()
 */
class CheckProfCharacter extends Character {


    public CheckProfCharacter() {
        this.cost = 2;
        this.maxCost = 3;
        description = "For this turn, you can gain Professors also if" +
                "you have THE SAME number of Students as your best opponent";
        this.number = 2;
        students = new ArrayList<>();

    }

    public synchronized void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Character.payandUpdateCost(player,cost, maxCost);

        player.setOrEqual(true);


    }

    public void reset (Game game, PlayerController pc){
        Player player = pc.getPlayer();
        player.setOrEqual(false);
    }
}
