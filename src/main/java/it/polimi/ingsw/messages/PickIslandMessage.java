package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.ViewImpls.IslandView;
import it.polimi.ingsw.model.GameMap;

public class PickIslandMessage extends Repliable{

    String text;

    public PickIslandMessage(GameMap gm){
        text = "This is the current state of the islands:\n" + gm +
                "\nIndicate the island by its number (0~"+(gm.getArchipelago().size()-1)+"):";
    }


    @Override
    public void switchAndFillView() {
        //show a screen where you can interact with the islands
        UIManager uim = UIManager.getUIManager();
        uim.getSwitcher().toIslands();
        ((IslandView)uim.getIslandView()).enableIslands();

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
