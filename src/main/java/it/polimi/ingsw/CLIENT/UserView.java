package it.polimi.ingsw.CLIENT;


import it.polimi.ingsw.CLIENT.ViewImpls.CLIView;
import it.polimi.ingsw.messages.Message;

public class UserView {
    //we will read the input from the user and send it
    View currentView;

    public void update() {
        currentView.display();
    }


    public void getUserInput() {
        //might send this to the local controller if we do it
        currentView.sendReply();
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public View getCurrentView() {
        return currentView;
    }
}

