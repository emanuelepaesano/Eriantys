package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.Objects;
import java.util.Scanner;

public class PlayerController {
    private int id;
    private Player player;


    public PlayerController(int id, int numPlayers, School school){
        this.id = id;
        player = new Player(id, numPlayers, school);
    }


    public Player getPlayer() {
        return player;
    }

}
