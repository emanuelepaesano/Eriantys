package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.LoginMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;

public class LoginView implements View {
    String content;

    String reply;

    //static buttons and stuff

    //accessed only by the user view with update()
    @Override
    public void display() {
        //this.setVisible(true);
    }

    @Override
    public Message getReply() {
        //add a text field with a scanner
        reply = scanner.nextLine();
        return new LoginMessage(reply);
    }

    @Override
    public void fillInfo(Message message) {
        //fill buttons/other component with content field info.
        content = (message).toString();
    }
}
