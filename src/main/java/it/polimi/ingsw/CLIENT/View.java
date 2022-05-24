package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.Serializable;
import java.net.URL;
import java.util.Scanner;

public interface View extends Serializable {
    void display();
    void sendReply();
    void fillInfo(Message message);

}
