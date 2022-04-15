package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class Cloud {
    private Map<StudColor, Integer> students;

    public Cloud(){
        this.students = Island.makeStudents();
    }

}
