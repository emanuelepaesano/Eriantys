package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.Message;

public class TestVirtualView extends VirtualView {
    @Override
    public synchronized void update(Message message) {}
}