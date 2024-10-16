package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * For this turn, >= instead of > in checkProfessors()
 */
class OrEqualCharacter extends Character {


    public OrEqualCharacter() {
        this.cost = 2;
        this.maxCost = 3;
        description = "For this turn, you can gain Professors also if" +
                " you have THE SAME number of Students as your best opponent.\nN.B.: You still need" +
                " to move at least one Student of that color to the Dining Room.";
        this.number = 2;
    }

    public boolean play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            Character.sendNoMoneyMessage(pc.getPlayerView());
            return false;}
        this.cost = Character.payandUpdateCost(player,cost, maxCost);
        player.setOrEqual(true);
        new NoReplyMessage(false,"Play Character","Effect active",
                "The Character effect was activated. You can use it for this turn only.").send(pc.getPlayerView());
        return true;
    }

    public void reset (Game game, PlayerController pc){
        Player player = pc.getPlayer();
        player.setOrEqual(false);
    }
}
