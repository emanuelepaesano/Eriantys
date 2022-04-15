package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Island {
    int id;
    Map<StudColor,Integer> students;
    Player owner;
    Boolean hasMother;
    //we might do a test that we never have 2 island with this set to true
    int size;

    //This is used also by the clouds and the tables to initialize
    public static Map<StudColor,Integer> makeStudents(){
        HashMap<StudColor,Integer> studs = new HashMap<>();
        for (StudColor sc : StudColor.values()){
            studs.put(sc,0);
        }
        return studs;
    }

    public Island(int id){
        this.id = id;
        size = 1;
        students = makeStudents();
        hasMother = false;
    }


    /**
     *
     * @return Map of all studColors to 0
     */

    //GETTERS SETTERS


    public void setHasMother(Boolean hasMother) {
        this.hasMother = hasMother;
    }

    public Map<StudColor, Integer> getStudents() {
        return students;
    }
}


