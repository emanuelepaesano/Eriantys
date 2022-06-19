package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class SwitcherMessage extends Repliable{

    String text;
    List<Player> players;

    public SwitcherMessage(List<Player> players) {
        this.players = players;
        text = "Which player's school do you want to see " +
                "(Players: " + players + ") ?\n" + "To return to action selection, type '0' or 'back'";
    }

    @Override
    public void switchAndFillView() {
        UIManager uim = UIManager.getUIManager();
        View switcherView = uim.getSwitcher();
        switcherView.fillInfo(this);
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

    public List<Player> getOtherPlayers() {
        return players;
    }
}
