package it.polimi.ingsw.CLIENT;

public class LocalModel {

    private GeneralViewState gameState;

    private ViewState currentState;

    private String message;

    public LocalModel(){
        currentState = new InitState();
    }

    public void setCurrentState(ViewState currentState) {
        this.currentState = currentState;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ViewState getCurrentState(){
        return currentState;
    }
    public GeneralViewState getGameState(){
        return gameState;
    }

    public void setGameState(GeneralViewState gameState) {
        this.gameState = gameState;
    }
}
