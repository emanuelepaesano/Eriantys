package it.polimi.ingsw.model;

import java.util.Arrays;

import java.util.List;

public class Entrance {
    // TODO: 12/04/2022 missing reference to the player.
    //  actually we may not need it
    private List<StudColor> students;
    private final int size;
    private DiningRoom diningRoom;

    public Entrance(int numplayers, DiningRoom diningRoom){
        size = (numplayers == 3 ? 9:7);
        this.diningRoom = diningRoom;

        //fixed size list, entries initialized to null
        students = Arrays.asList(new StudColor[size]);
    }

    private void moveToDiningRoom(){}


    @Override
    public String toString() {
        return "Entrance{" +
                "students =" + students +
                '}';
    }

    public List<StudColor> getStudents() {
        return students;
    }
}
