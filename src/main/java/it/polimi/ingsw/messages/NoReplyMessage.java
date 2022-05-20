package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;

import java.util.List;

public class NoReplyMessage implements Message {
    final String content;
    public NoReplyMessage(String content) {
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
        return "simpledialog";
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
        return this.content;
    }

    public Boolean isRepliable() {
        return false;
    }

    public String getContent() {
        return content;
    }
}
