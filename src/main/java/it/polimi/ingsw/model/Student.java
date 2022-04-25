package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;
// TODO: 11/04/2022
//  -> add abbreviations

public enum Student {
    YELLOW,
    BLUE,
    RED,
    PINK,
    GREEN,
    ;

    public static HashMap<Student, Integer> makeStudents() {
        HashMap<Student, Integer> studs = new HashMap<>();
        for (Student sc : Student.values()) {
            studs.put(sc, 0);
        }
        return studs;
    }
}


