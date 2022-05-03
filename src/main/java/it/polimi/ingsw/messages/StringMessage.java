package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.Message;

public class StringMessage extends Message {
    String content;
    VirtualView user;


    public StringMessage(String content) {
        this.content = content;
        this.user = user;
    }

    @Override
    public void send() {
        user.update(content);
    }

    @Override
    public String toString() {
        return this.content;
    }
}
