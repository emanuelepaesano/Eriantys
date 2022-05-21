package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class ActionPhaseMessage extends Repliable implements Message{

    String text;
    Player player;
    List<Character> characters;
    String type;
    Integer availableActions;

    public ActionPhaseMessage(Boolean advanced, int availableActions, Player player, List<Character>characters) {
        //we can show a popup saying that it is your turn (or something like that), and enable two (or three) buttons
        //in the "yourentrance" screen
        this.availableActions = availableActions;
        this.player = player;
        this.characters = characters;
        this.type = "yourturn";
        if(advanced) {
            text = (player.getPlayerName()) + ", choose an action. (" + availableActions + " moves left)" +
                    " Please type \"islands\" or \"diningroom\" to move students, or \"characters\" to play a character. ";
        }
        else text = (player.getPlayerName()) + ", choose an action. (" + availableActions + " moves left)" +
                " Please type \"islands\" or \"diningroom\" to move students. ";
    }

    public ActionPhaseMessage(Player player, int availableActions) {
        //questo messaggio deve aprire un menu con una rotella per scegliere numero
        this.availableActions = availableActions;
        this.player = player;
        this.type = "howmany";
        text = "How many students do you want to move " +
                "(maximum " + availableActions+ ") ?\n" + "To return to action selection, type '0' or 'back'";
    }

    @Override
    public void send(VirtualView user) {
        user.update(this);
    }

    @Override
    public void send(List<VirtualView> all) {
        all.forEach(v->v.update(this));
    }

    @Override
    public String getView() {
        return "actionview";
    }

    @Override
    public void switchAndFillView() {

    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public Boolean isRepliable() {
        return true;
    }


}
