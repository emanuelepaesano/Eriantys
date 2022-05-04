package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;

import java.util.List;

public class ActionPhaseMessage implements Message{
    @Override
    public void send(VirtualView user) {

    }

    @Override
    public void send(List<VirtualView> all) {

    }

    @Override
    public String getView() {
        return "actionview";
    }
}
