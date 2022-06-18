package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.Message;

import java.util.List;
import java.util.function.Consumer;

public class StringMessage extends Repliable {
    final String content;
    String reply = "";


    public StringMessage(String content) {
        this.content = content;
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
        return true;
    }

    @Override
    public void setReply(String s) {
        this.reply = s;
    }

    @Override
    public String getReply() {
        return reply;
    }
}
