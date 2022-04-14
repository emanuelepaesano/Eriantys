package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.Entrance;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class PlayerController {
    private int id;
    protected Player player;


    PlayerController(int id, int numplayers){
        //the id of the playercontroller and its player coincide, might turn out useful
        this.id = id;
        DiningRoom d = new DiningRoom();
        Entrance e = new Entrance(numplayers, d);
        player = new Player(id,d,e);
        //it's important to make the dining room first
    }

}
