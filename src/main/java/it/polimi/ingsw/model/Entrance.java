package it.polimi.ingsw.model;

import java.util.Arrays;

import java.util.List;

public class Entrance {
    // TODO: 12/04/2022 missing reference to the player
    private List<StudColor> students;
    private final int size;

    public Entrance(Game game){
        size = (game.getNumPlayers() == 3 ? 9:7);

        //fixed size list, entries initialized to null
        students = Arrays.asList(new StudColor[size]);

    }

}
