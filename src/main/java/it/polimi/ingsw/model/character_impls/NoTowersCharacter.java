package it.polimi.ingsw.model.character_impls;

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
        //for this turn, for checkowner(), size does not matter :)
        Player thisTurn = game.getCurrentPlayer();
        List<Island> islands = game.getGameMap().getArchipelago();
        List<Integer> oldsizes = islands.stream().map(Island::getSize).toList();

        while (game.getCurrentPlayer() == thisTurn) {
            //we either make size 0 or  change the checkowner
            //if (islands.get(0).getSize() != 0){
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
        System.out.println("Thread finished!");
    }

    public int getCost() {
        return cost;
    }
}
