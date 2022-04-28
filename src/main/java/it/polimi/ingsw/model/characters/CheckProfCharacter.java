package it.polimi.ingsw.model.characters;

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

    public synchronized void play(Player player, Game game) throws InterruptedException {
        if (!Character.enoughMoney(player,cost)){return;}
        this.cost = Character.payandUpdateCost(player,cost);
        Player thisTurn = game.getCurrentPlayer();
        while (game.getCurrentPlayer() == thisTurn) {
            player.setOrEqual(true);
            wait();
        }
        player.setOrEqual(false);
    }

    public int getCost() {
        return cost;
    }
}
