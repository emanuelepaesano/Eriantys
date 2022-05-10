package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.VirtualView;

import java.io.Serializable;
import java.util.List;

public interface Message extends Serializable {

    static Message receive (VirtualView user){
        return (Message) user.getAnswer();
    }

    void send(VirtualView user);

    void send(List<VirtualView> all);
    String getView();

    void switchAndFillView(Message this);
    Boolean isPing();



}





