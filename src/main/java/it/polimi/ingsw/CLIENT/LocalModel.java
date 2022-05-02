package it.polimi.ingsw.CLIENT;

public class LocalModel {

    private ViewState state;

    private String message;

    public LocalModel(){
        state = new InitState();
    }

    public void setState(ViewState state) {
        this.state = state;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ViewState getState(){
        return state;
    }
}
