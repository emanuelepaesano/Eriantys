package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.VirtualView;

import java.util.List;

public class StartGameMessage implements Message{
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
        return null;
    }

    @Override
    public void switchAndFillView() {
        UIManager uim = UIManager.getUIManager();
        uim.getSwitcher().display();
    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public Boolean isRepliable() {
        return false;
    }
}
