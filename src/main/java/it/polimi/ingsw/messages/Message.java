package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;

import java.io.Serializable;
import java.util.List;

public interface Message extends Serializable {


    void send(VirtualView user);

    void send(List<VirtualView> all);
    String getView();

    void switchAndFillView();
    Boolean isPing();

    Boolean isRepliable();


}





