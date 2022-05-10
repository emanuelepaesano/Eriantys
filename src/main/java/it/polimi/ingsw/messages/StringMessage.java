package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.Message;

import java.util.List;

public class StringMessage implements Message {
    final String content;

    String answer = "";


    public StringMessage(String content) {
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
        return "simpleview";
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

    public String getContent() {
        return content;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
