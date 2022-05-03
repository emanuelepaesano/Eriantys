package it.polimi.ingsw;

import java.io.IOException;
import java.io.Serializable;

public abstract class Message implements Serializable {

    public abstract void send();

    public Message receive(VirtualView user) throws IOException, ClassNotFoundException {
        return user.getAnswer();
    }

}





