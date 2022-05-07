package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * For this turn, >= instead of > in checkProfessors()
 */
class CheckProfCharacter extends Characters {
    int cost;


    public CheckProfCharacter() {
        this.cost = 2;


    }

    public synchronized void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Character.payandUpdateCost(player,cost);
        Player thisTurn = game.getCurrentPlayer();
        Thread t = new Thread(()->{
            while (game.getCurrentPlayer() == thisTurn) {
                player.setOrEqual(true);
                try{
                    wait();
                } catch (InterruptedException ex){Thread.currentThread().interrupt();}
            }
            player.setOrEqual(false);
            System.out.println("Thread finished!");
        });
        t.start();
    }

    public int getCost() {
        return cost;
    }
}
