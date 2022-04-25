package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

public class PlayerController {
    private int id;
    private Player player;


    public PlayerController(int id, int numPlayers){
        this.id = id;
        PlayerFactory pf = new PlayerFactory();
        player = pf.makePlayer(id, numPlayers);
    }


    public Player getPlayer() {
        return player;
    }

}
