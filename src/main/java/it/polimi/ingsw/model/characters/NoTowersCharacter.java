package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * This turn towers will not count towards influence
 */
class NoTowersCharacter extends Characters {
    int cost;

    public NoTowersCharacter() {
        this.cost = 3;
    }

    public synchronized void play(Player player, Game game) throws InterruptedException {
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Character.payandUpdateCost(player,cost);
        Player thisTurn = game.getCurrentPlayer();
        List<Island> islands = game.getGameMap().getArchipelago();
        List<Integer> oldsizes = islands.stream().map(Island::getSize).toList();
        while (game.getCurrentPlayer() == thisTurn) {
            //we either make size 0 or  change the checkowner
            islands.replaceAll(island -> {
                island.setSize(0);
                return island;
            });
            wait();
        }
        islands.replaceAll(island -> {
            island.setSize(oldsizes.get(islands.indexOf(island)));
            return island;
        });
    }

    public int getCost() {
        return cost;
    }
}
