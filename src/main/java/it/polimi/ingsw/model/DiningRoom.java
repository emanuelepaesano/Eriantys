package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class DiningRoom {
    // TODO: 12/04/2022 reference to player
    //  we may not need it for now
    private Map<StudColor, Integer> tables;
    private Map<StudColor, Boolean> professors;

    //maybe the entrance can contain a reference to the dining room
    //so we dont pass through player every time. then we make the dining room first
    public DiningRoom(){
        this.tables = Island.makeStudents();
        this.professors= makeProfessors();
    }


    /**
     * @return initializes tables with 0 students
     */
    private Map<StudColor,Integer> makeTables(){
     tables = new HashMap<>();
     for (StudColor sc : StudColor.values()){
         tables.put(sc,0);
     }
     return tables;
    }

    /**
     *
     * @return initializes all professors to false
     */
    private Map<StudColor,Boolean> makeProfessors(){
        professors = new HashMap<>();
        for (StudColor sc : StudColor.values()){
            professors.put(sc,false);
        }
        return professors;
    }

    public Map<StudColor, Integer> getTables() {
        return tables;
    }
}
