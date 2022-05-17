package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.TowerColor;

import java.util.List;

public class LoginMessage extends Repliable implements Message{

    String content;
    String reply;
    List<TowerColor> availableColors;

    public LoginMessage(String content) {
        this.content = content;
    }

    public LoginMessage(String content, List<TowerColor> availablecolors){
        this.content = content;
        this.availableColors = availablecolors;
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
        UIManager uim = UIManager.getUIManager();
        uim.getLoginView().fillInfo(this);
        uim.getLoginView().display();
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public String getReply() {
        return reply;
    }

    @Override
    public void setReply(String s) {
        this.reply = s;
    }

    @Override
    public Boolean isRepliable() {
            return true;
    }

}
