package it.polimi.ingsw.model.characters;

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
        if (!Character.enoughMoney(player,cost)){
            System.out.println("You don't have enough money!");
            return;}
        this.cost = Character.payandUpdateCost(player,cost);
        Player thisTurn = game.getCurrentPlayer();
        while (game.getCurrentPlayer() == thisTurn) {
            player.setBaseMoves(2);
            wait();
        }
        player.setBaseMoves(0);
    }

    public int getCost() {
        return cost;
    }
}
