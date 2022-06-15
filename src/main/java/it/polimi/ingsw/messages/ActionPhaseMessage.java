package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.characters.Characters;
import it.polimi.ingsw.model.Player;

import java.util.List;

import static it.polimi.ingsw.messages.ActionPhaseMessage.ActionPhaseType.*;

public class ActionPhaseMessage extends Repliable implements Message{

    String text;
    Player player;
    List<Characters> characters;
    ActionPhaseType type;
    Integer availableActions;

    public ActionPhaseMessage(Boolean advanced, int availableActions, Player player, List<Characters>characters) {
        //we can show a popup saying that it is your turn (or something like that), and enable two (or three) buttons
        //in the "yourentrance" screen
        this.availableActions = availableActions;
        this.player = player;
        this.characters = characters;
        this.type = yourturn;
        if(advanced) {
            text = (player.getPlayerName()) + ", choose an action. (" + availableActions + " moves left)" +
                    " Please type \"islands\" or \"diningroom\" to move students, or \"characters\" to play a character. ";
        }
        else text = (player.getPlayerName()) + ", choose an action. (" + availableActions + " moves left)" +
                " Please type \"islands\" or \"diningroom\" to move students. ";
    }

    public ActionPhaseMessage(Player player, int availableActions) {
        this.availableActions = availableActions;
        this.player = player;
        this.type = howmany;
        text = "How many students do you want to move " +
                "(maximum " + availableActions+ ") ?\n" + "To return to action selection, type '0' or 'back'";
    }

    public ActionPhaseMessage(Player player, ActionPhaseType type) {
        this.player = player;
        this.type = type;
        if (type.equals(studselect)){
            text = "Choose a student color from the available ones:\n{";
            for (Student student : player.getEntrance().getStudents()) {
                text += "(" + student + ")";
            }
            text += "} or type \"back\" to annull.";
        }
        else {text = player.getEntrance().toString();}
    }

    public enum ActionPhaseType {
        yourturn,
        howmany,
        update,
        studselect,
        endActions;
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
        UIManager uim = UIManager.getUIManager();
        View apv = uim.getSchoolView();
        apv.fillInfo(this);
//        apv.display();
        uim.getSwitcher().toSchool();
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

    public Player getPlayer() {
        return player;
    }

    public ActionPhaseType getType() {
        return type;
    }

    public Integer getAvailableActions() {
        return availableActions;
    }
}
