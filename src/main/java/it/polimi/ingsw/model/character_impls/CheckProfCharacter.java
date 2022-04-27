package it.polimi.ingsw.model.character_impls;

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

    public void play(Player player, Game game) throws InterruptedException {
        Player thisTurn = game.getCurrentPlayer();
        while (game.getCurrentPlayer() == thisTurn) {
            player.setOrEqual(true);
            synchronized (this) {
                wait();
            }
        }
        player.setOrEqual(false);
        this.cost += 1;
        System.out.println("Thread finished!");
    }

    public int getCost() {
        return cost;
    }
}
