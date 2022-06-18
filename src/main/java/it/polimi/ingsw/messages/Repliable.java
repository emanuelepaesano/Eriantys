package it.polimi.ingsw.messages;

public abstract class Repliable extends Message {

    String reply;
    public void setReply(String s){this.reply = s;}

    public String getReply(){return reply;}
}
