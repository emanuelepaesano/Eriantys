package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.GenInfoMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.GameMap;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class GenInfoView implements View {

    GameMap map;
    List<Player> players;

    //screen with the map and the player schools (static)
    @Override
    public void display() {
         //setVisible(true)
    }

    @Override
    public void sendReply() {
    }

    @Override
    public void fillInfo(Message message) {
        GenInfoMessage mess = (GenInfoMessage) message;
        map = mess.getMap();
        players = mess.getPlayers();

    }
}
