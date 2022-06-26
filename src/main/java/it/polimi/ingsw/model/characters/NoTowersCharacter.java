package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * This turn towers will not count towards influence
 */
class NoTowersCharacter extends Character {

    public List<Integer> getOldsizes() {
        return oldsizes;
    }

    List<Integer> oldsizes;

    public NoTowersCharacter() {
        this.cost = 3;
        this.maxCost = 4;
        description="When resolving an island, Towers will not count towards influence for this turn.";
        this.number = 6;
    }
    public void play(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Character.payandUpdateCost(player,cost,maxCost);
        List<Island> islands = game.getGameMap().getArchipelago();
        oldsizes = islands.stream().map(Island::getSize).toList();
            //we either make size 0 or  change the checkowner
        islands.forEach(island -> island.setSize(0));
    }

    public void reset(Game game, PlayerController pc){
        List<Island> islands = game.getGameMap().getArchipelago();
        islands.forEach(island ->
                island.setSize(oldsizes.get(islands.indexOf(island)))
        );
        oldsizes = null;
    }
}
