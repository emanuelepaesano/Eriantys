package it.polimi.ingsw.messages;

public abstract class Repliable implements Message{

    String reply;
    public void setReply(String s){this.reply = s;}

    public String getReply(){return reply;}
}
