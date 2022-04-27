package it.polimi.ingsw.model.character_impls;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * For this turn, player has +2 Mother Nature movements
 */
class MoreMovementsCharacter extends Characters {
    int cost;

    public MoreMovementsCharacter() {
        this.cost = 1;
    }

    public synchronized void play(Player player, Game game) throws InterruptedException {
        Player thisTurn = game.getCurrentPlayer();
        while (game.getCurrentPlayer() == thisTurn) {
            player.setBaseMoves(2);
            wait();
        }
        player.setBaseMoves(0);
        this.cost += 1;
        System.out.println("Thread finished!");
    }

    public int getCost() {
        return cost;
    }
}
