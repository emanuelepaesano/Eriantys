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
    private Game game;


    public PlayerController(int id, Game game){ //if this gives weird errors, go back to int numplayers instead of game
        //the id of the playercontroller and its player coincide
        this.id = id;
        this.game = game;
        int numPlayers = game.numPlayers;
        player = new Player(id,game);


    }


    public Player getPlayer() {
        return player;
    }

}
