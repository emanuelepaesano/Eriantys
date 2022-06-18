package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;

import java.util.List;

public class PingMessage extends Message {


    @Override
    public void switchAndFillView() {

    }

    @Override
    public Boolean isPing() {
        return true;
    }

    @Override
    public Boolean isRepliable() {
        return false;
    }
}
