package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * For this turn, player has +2 Mother Nature movements
 */
class MoreMovementsCharacter extends Character {

    public MoreMovementsCharacter( ) {
        this.cost = 1;
        this.maxCost = 2;
        description="You may move Mother Nature up to 2 additional islands than is indicated" +
                " by the Assistant you played.";
        this.number = 4;

    }

    public boolean play(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            Character.sendNoMoneyMessage(pc.getPlayerView());
            return false;}
        this.cost = Character.payandUpdateCost(player,cost,maxCost);
        player.setBaseMoves(2);
        return true;
    }

    public void reset(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        player.setBaseMoves(0);
    }

}
