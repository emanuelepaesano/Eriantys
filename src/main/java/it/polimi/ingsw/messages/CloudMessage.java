package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.ViewImpls.IslandView;
import it.polimi.ingsw.VirtualView;

import java.util.List;

public class CloudMessage extends Repliable{

    String content;
    public CloudMessage(String content) {
        this.content = content;
    }


    @Override
    public void switchAndFillView() {
//        UIManager uim = UIManager.getUIManager();
//        uim.getSwitcher().toIslands();
//        ((IslandView)uim.getGenInfoView()).enableClouds();

    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public Boolean isRepliable() {
        return true;
    }
}
