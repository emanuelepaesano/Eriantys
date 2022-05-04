package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;

import java.io.Serializable;
import java.util.Scanner;

public interface View extends Serializable {
    Scanner scanner = new Scanner(System.in);

    void display();

    Message getReply();
    void fillInfo(Message message);

}
