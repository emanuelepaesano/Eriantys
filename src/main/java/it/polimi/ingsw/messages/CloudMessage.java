package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.ViewImpls.IslandView;
import it.polimi.ingsw.VirtualView;

import java.util.List;

public class CloudMessage extends Repliable implements Message{

    String content;
    public CloudMessage(String content) {
        this.content = content;
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
        return "cloudselection";
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
