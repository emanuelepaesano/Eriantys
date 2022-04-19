package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class Island {
    int id;
    Map<Student,Integer> students;
    Player owner;
    int size;

    /**
     *
     * @return Map of all StudColors to 0. This method is used also by the DiningRoom to initialize, hence it's static.
     */
    public static Map<Student,Integer> makeStudents(){
        HashMap<Student,Integer> studs = new HashMap<>();
        for (Student sc : Student.values()){
            studs.put(sc,0);
        }
        return studs;
    }

    public Island(int id){
        this.id = id;
        size = 1;
        students = makeStudents();
    }



    //GETTERS SETTERS

    public Map<Student, Integer> getStudents() {
        return students;
    }
}


