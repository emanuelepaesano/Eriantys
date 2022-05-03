package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.messages.Message;

public class LocalModel {


    private ViewState currentState;

    private Message message;

    public LocalModel(){
        currentState = new InitState();
    }

    public void setCurrentState(ViewState currentState) {
        this.currentState = currentState;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public ViewState getCurrentState(){
        return currentState;
    }

}
