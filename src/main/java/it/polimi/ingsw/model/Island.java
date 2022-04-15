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
    private Map<StudColor,Integer> makeStudents(){
        students = new HashMap<>();
        for (StudColor sc : StudColor.values()){
            students.put(sc,0);
        }
        return students;
    }


    //GETTERS SETTERS


    public void setHasMother(Boolean hasMother) {
        this.hasMother = hasMother;
    }

    public Map<StudColor, Integer> getStudents() {
        return students;
    }
}


