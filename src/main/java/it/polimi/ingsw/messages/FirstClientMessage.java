package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.VirtualView;

import java.util.List;

public class FirstClientMessage implements Message {

    String text;
    public FirstClientMessage(String s) {
        text = s;
    }

    @Override
    public void send(VirtualView user) {
        user.update(this);
    }

    @Override
    public void send(List<VirtualView> all) {
        all.forEach((v)->v.update(this));
    }


    @Override
    public String getView() {
        return "firstclient";
    }

    @Override
    public void switchAndFillView() {
        UIManager uim = UIManager.getGuiManager();
        uim.getFirstClientView().display(uim.getFirstClientRoot());
    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public String toString() {
        return text;
    }
}
