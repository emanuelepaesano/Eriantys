package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
// TODO: 11/04/2022
//  -> add abbreviations

public enum Student {
    YELLOW,
    BLUE,
    RED,
    PINK,
    GREEN;

    /**
     *
     * @return Map of all StudColors to 0. This method is used also by the DiningRoom to initialize, hence it's static.
     */
    public static Map<Student,Integer> makeStudents(){
        HashMap<it.polimi.ingsw.model.Student, java.lang.Integer> studs = new HashMap<>();
        for (it.polimi.ingsw.model.Student sc : it.polimi.ingsw.model.Student.values()){
            studs.put(sc,0);
        }
        return studs;
    }

}