package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.ViewImpls.GenInfoView;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.GameMap;

import java.util.List;

public class PickIslandMessage extends Repliable{

    String text;

    public PickIslandMessage(GameMap gm){
        text = "This is the current state of the islands:\n" + gm +
                "\nIndicate the island by its number (0~"+(gm.getArchipelago().size()-1)+"):";
    }

    @Override
    public void send(VirtualView user) {user.update(this);}

    @Override
    public void send(List<VirtualView> all) {all.forEach((u)->u.update(this));}

    @Override
    public String getView() {
        return "";
    }

    @Override
    public void switchAndFillView() {
        //show a screen where you can interact with the islands
        UIManager uim = UIManager.getUIManager();
        uim.getSwitcher().toIslands();
        ((GenInfoView)uim.getGenInfoView()).enableIslands();

    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public Boolean isRepliable() {
        return true;
    }

    @Override
    public String toString() {
        return text;
    }
}
