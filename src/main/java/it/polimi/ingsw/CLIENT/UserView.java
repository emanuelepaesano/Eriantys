package it.polimi.ingsw.CLIENT;


import it.polimi.ingsw.CLIENT.ViewImpls.CLIView;
import it.polimi.ingsw.messages.Message;

public class UserView {
    //we will read the input from the user and send it
    View currentView;

    public UserView(Boolean gui) {
        if (!gui){
            this.currentView = new CLIView();
        }
    }

    public void update() {
        currentView.display();
    }


    public Message getUserInput() {
        //might send this to the local controller if we do it
        return currentView.getReply();
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public View getCurrentView() {
        return currentView;
    }
}

