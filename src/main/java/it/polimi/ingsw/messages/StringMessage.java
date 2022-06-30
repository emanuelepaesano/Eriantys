package it.polimi.ingsw.messages;

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
