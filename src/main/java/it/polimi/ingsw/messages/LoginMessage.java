package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.VirtualView;

import java.util.List;

public class LoginMessage implements Message{

    String content;

    public LoginMessage(String content) {
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
        return "loginview";
    }

    @Override
    public void switchAndFillView() {
        UIManager uim = UIManager.getGuiManager();
        uim.getLoginView().fillInfo(this);
        uim.getLoginView().display(uim.getLoginRoot());
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public Boolean isPing() {
        return false;
    }
}
