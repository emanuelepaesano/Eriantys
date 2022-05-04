package it.polimi.ingsw.controller.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * For this turn, >= instead of > in checkProfessors()
 */
class CheckProfCharacter extends Characters {
    int cost;
    Game game;

    public CheckProfCharacter(Game game) {
        this.cost = 2;
        this.game=game;

    }

    public synchronized void play(Player player) throws InterruptedException {
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
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
