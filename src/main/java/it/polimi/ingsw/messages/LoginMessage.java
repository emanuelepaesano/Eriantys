package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.model.TowerColor;

import java.util.List;

public class LoginMessage extends Repliable{

    String content;
    String reply;
    List<TowerColor> availableColors;
    List<Integer> availableWiz;
    String type;


    public LoginMessage(String content){
        this.content = content;
        type = "name";
    }

    public LoginMessage(String content, List<TowerColor> availableTowers){
        this.content = content;
        this.availableColors = availableTowers;
        this.type = "tower";
    }

    public LoginMessage(List<Integer> availableWiz, String content){
        this.content = content;
        this.availableWiz = availableWiz;
        this.type = "wizard";
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

    public String getType() {
        return type;
    }

    public List<TowerColor> getAvailableColors() {
        return availableColors;
    }

    public List<Integer> getAvailableWiz() {
        return availableWiz;
    }
}
