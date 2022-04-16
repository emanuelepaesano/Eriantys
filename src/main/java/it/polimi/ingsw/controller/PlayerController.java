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
    private int numPlayers;
    private Game game;


    public PlayerController(int id, Game game){ //if this gives weird errors, go back to int numplayers instead of game
        //the id of the playercontroller and its player coincide, might turn out useful
        this.id = id;
        this.game = game;
        numPlayers = game.numPlayers;
        player = new Player(id,game);


        //it's important to make the dining room before the entrance
    }


    public Player getPlayer() {
        return player;
    }

}
