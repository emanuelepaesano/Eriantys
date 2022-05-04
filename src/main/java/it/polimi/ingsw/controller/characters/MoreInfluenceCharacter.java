package it.polimi.ingsw.controller.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * This turn player has +2 influence
 */
class MoreInfluenceCharacter extends Characters {

    int cost;
    Game game;

    public MoreInfluenceCharacter(Game game) {
        this.cost = 1;
        this.game=game;
    }

    public synchronized void play(Player player) throws InterruptedException {
        //this turn +2 influence (n.b. you cant combine characters)
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Character.payandUpdateCost(player,cost);
        Player thisTurn = game.getCurrentPlayer();
        while (game.getCurrentPlayer() == thisTurn) {
            player.setBaseInfluence(2);
            wait();
        }
        player.setBaseInfluence(0);
    }

    public int getCost() {
        return cost;
    }

}
