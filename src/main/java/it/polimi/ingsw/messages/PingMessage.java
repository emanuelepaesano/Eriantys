package it.polimi.ingsw.messages;

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
