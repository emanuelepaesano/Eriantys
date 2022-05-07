package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * This turn player has +2 influence
 */
class MoreInfluenceCharacter extends Characters {

    int cost;

    public MoreInfluenceCharacter() {
        this.cost = 1;
    }

    public void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        //this turn +2 influence (n.b. you cant combine characters)
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Character.payandUpdateCost(player,cost);
        Player thisTurn = game.getCurrentPlayer();
        Thread t = new Thread(()->{
            while (game.getCurrentPlayer() == thisTurn) {
            player.setBaseInfluence(2);
            try{
                synchronized (this){wait();}
            } catch (InterruptedException ex){Thread.currentThread().interrupt();}
        }
        player.setBaseInfluence(0);
        System.out.println("Thread finished!");
        });
        t.start();
    }

    public int getCost() {
        return cost;
    }

}
