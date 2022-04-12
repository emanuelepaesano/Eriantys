package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;

public class PlayerController {
    private int id;
    private Player player;

    PlayerController(int id){
        //the id of the playercontroller and its player coincide, might turn out useful
        this.id = id;
        this.player = new Player(id);
    }

}
