package it.polimi.ingsw.model;

import java.util.*;

public class Cloud {
    final List<Student> students;
    int size;
    //Actually i don't really like the map, a list would be better since only 3 or 4 positions
    public Cloud(Game game){
        size = (game.numPlayers == 3 ? 4:3);
        //I like this better, since we only have 3 or 4 positions
        students = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "\n Cloud: "
                + students;
    }

    public int getSize() {
        return size;
    }

    public List<Student> getStudents() {
        return students;
    }
}
