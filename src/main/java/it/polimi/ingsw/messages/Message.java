package it.polimi.ingsw.messages;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Message implements Serializable {


    public void sendAndCheck(VirtualView user) throws DisconnectedException {
        if (user.isDisconnected()){throw new DisconnectedException();}
        else {user.update(this);}
    }

    public void send(VirtualView user) {
        user.update(this);
    }

    public void send(List<VirtualView> all) {
        List<VirtualView> activeViews = all.stream().filter(v -> !v.isDisconnected()).toList();
        activeViews.forEach(v->v.update(this));
    }


    public abstract void switchAndFillView();
    public abstract Boolean isPing();

    public abstract Boolean isRepliable();


}





