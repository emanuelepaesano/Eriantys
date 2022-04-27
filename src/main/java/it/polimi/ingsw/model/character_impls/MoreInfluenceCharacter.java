package it.polimi.ingsw.model.character_impls;

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

    public synchronized void play(Player player, Game game) throws InterruptedException {
        //this turn +2 influence (n.b. you cant combine characters)
        if (!Characters.enoughMoney(player,cost)){return;}
        this.cost = Characters.payandUpdateCost(player,cost);
        Player thisTurn = game.getCurrentPlayer();
        while (game.getCurrentPlayer() == thisTurn) {
            player.setBaseInfluence(2);
            wait();
        }
        player.setBaseInfluence(0);
        System.out.println("Thread finished!");
    }

    public int getCost() {
        return cost;
    }

}
