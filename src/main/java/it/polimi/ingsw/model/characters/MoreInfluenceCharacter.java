package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * This turn player has +2 influence
 */
class MoreInfluenceCharacter extends Character {

    public MoreInfluenceCharacter() {
        this.cost = 1;
        this.maxCost = 2;
        description="You will have +2 Influence on Islands for this turn.";
        this.number = 3;
    }

    public boolean play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        //this turn +2 influence (n.b. you cant combine characters)
        if (!Character.enoughMoney(player,cost)){
            Character.sendNoMoneyMessage(pc.getPlayerView());
            return false;}
        this.cost = Character.payandUpdateCost(player,cost,maxCost);
        player.setBaseInfluence(2);
        return true;
    }

    public void reset(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        player.setBaseInfluence(0);
    }

}
