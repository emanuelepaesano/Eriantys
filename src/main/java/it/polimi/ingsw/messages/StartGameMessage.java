package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;

public class StartGameMessage extends Message {


    @Override
    public void switchAndFillView() {
        UIManager uim = UIManager.getUIManager();
        uim.getSwitcher().display();
    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public Boolean isRepliable() {
        return false;
    }
}
