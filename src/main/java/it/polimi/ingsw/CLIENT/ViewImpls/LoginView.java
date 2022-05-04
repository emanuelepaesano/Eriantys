package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.LoginMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;

public class LoginView implements View {
    String content;

    String reply;


    //accessed only by the user view with update()
    @Override
    public void display() {
        System.out.println(content);
    }

    @Override
    public Message getReply() {
        reply = scanner.nextLine();
        return new LoginMessage(reply);
    }

    @Override
    public void fillInfo(Message message) {
        content = (message).toString();
    }
}
