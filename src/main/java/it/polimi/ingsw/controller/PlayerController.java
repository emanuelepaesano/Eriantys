package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.Entrance;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.Objects;
import java.util.Scanner;

public class PlayerController {
    private int id;
    private Player player;


    public PlayerController(int id, int numPlayers){
        this.id = id;
        player = new Player(id,numPlayers);


    }


    public Player getPlayer() {
        return player;
    }

}
