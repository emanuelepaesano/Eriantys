package it.polimi.ingsw.model;

import java.util.*;

public class Cloud {
    final List<StudColor> students;
    int size;
    //Actually i don't really like the map, a list would be better since only 3 or 4 positions
    public Cloud(Game game){
        size = (game.getNumPlayers() == 3 ? 4:3);
        //I like this better, since we only have 3 or 4 positions
        students = new ArrayList<>(Arrays.asList(new StudColor[size]));
    }

    @Override
    public String toString() {
        return "\n Cloud: "
                + students;
    }

    public int getSize() {
        return size;
    }

    public List<StudColor> getStudents() {
        return students;
    }
}
