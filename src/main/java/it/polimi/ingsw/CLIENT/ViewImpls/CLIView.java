package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;

public class CLIView implements View {

    String content;
    String reply;
    @Override
    public void display() {
        System.out.println(content);
    }

    @Override
    public Message getReply() {
        reply = scanner.nextLine();
        return new StringMessage(reply);
    }

    @Override
    public void fillInfo(Message message) {
        content = message.toString();
    }
}
