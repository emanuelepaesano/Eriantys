package it.polimi.ingsw.messages;

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
