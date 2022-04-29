package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.net.Socket;

public class PlayerController {
    private int id;
    private Player player;

    //Socket client = server.getClients.get(id-1)
    //we have to associate each player with a socket, so we can send messages
    //only to him. so like a method server.update(string,socket) to send only to that
    //as opposed to server.update(string) that sends to all(?)

    public PlayerController(int id, int numPlayers){
        this.id = id;
        player = Player.makePlayer(id, numPlayers);
    }


    public Player getPlayer() {
        return player;
    }

}
