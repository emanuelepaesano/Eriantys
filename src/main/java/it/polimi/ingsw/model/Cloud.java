package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class Cloud {
    private Map<StudColor, Integer> students;

    public Cloud(){
        this.students = getDefaultStudents();;
    }

    /**
     *
     * @return Map of all studColors to 0
     */
    private Map<StudColor,Integer> getDefaultStudents(){
        students = new HashMap<>();
        for (StudColor sc: StudColor.values()){
            students.put(sc,0);
        }
        return students;
    }

}
