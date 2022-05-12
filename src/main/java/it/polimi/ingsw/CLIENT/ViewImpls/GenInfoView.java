package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.GenInfoMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.GameMap;
import it.polimi.ingsw.model.Player;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class GenInfoView implements View {

    GameMap map;
    List<Player> players;

    @Override
    public void display() {
    }

    @Override
    public void display(Parent root) {

    }

    @Override
    public void sendReply() {
    }

    @Override
    public void fillInfo(Message message) {
        GenInfoMessage mess = (GenInfoMessage) message;
        map = mess.getMap();
        players = mess.getPlayers();
        //Here we have to link the elements from the model to the graphic components.

    }

}
