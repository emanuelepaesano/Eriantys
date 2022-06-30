package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;

public class FirstClientMessage extends Repliable {

    String text;
    private String reply;

    public FirstClientMessage(String s) {
        text = s;
    }




    @Override
    public void switchAndFillView() {
        UIManager uim = UIManager.getUIManager();
        uim.getFirstClientView().display();
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
    public Boolean isRepliable(){return true;}

    @Override
    public void setReply(String s) {
        this.reply = s;
    }

    @Override
    public String getReply() {
        return reply;
    }
}
