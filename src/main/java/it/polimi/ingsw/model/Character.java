package it.polimi.ingsw.model;

import java.util.Stack;

public interface Character {

     void play(Player player, Game game) throws InterruptedException;

     int getCost();

}
