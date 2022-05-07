package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
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

    public synchronized void play(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Character.payandUpdateCost(player,cost);
        Player thisTurn = game.getCurrentPlayer();
        List<Island> islands = game.getGameMap().getArchipelago();
        List<Integer> oldsizes = islands.stream().map(Island::getSize).toList();
        Thread t = new Thread(()->{
            while (game.getCurrentPlayer() == thisTurn) {
            //we either make size 0 or  change the checkowner
            islands.forEach(island -> island.setSize(0));
                try {
                    wait();
                } catch (InterruptedException e) {Thread.currentThread().interrupt();}
            }
            islands.forEach(island ->
                island.setSize(oldsizes.get(islands.indexOf(island)))
            );});
        t.start();
    }

    public int getCost() {
        return cost;
    }
}
