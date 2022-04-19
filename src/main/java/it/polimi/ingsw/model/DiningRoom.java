package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class DiningRoom {
    // TODO: 12/04/2022 reference to player
    //  we may not need it for now
    private Map<Student, Integer> tables;
    private Map<Student, Boolean> professors;

    //maybe the entrance can contain a reference to the dining room
    //so we dont pass through player every time. then we make the dining room first
    public DiningRoom(){
        this.tables = Island.makeStudents();
        this.professors= makeProfessors();
    }


    /**
     *
     * @return initializes all professors to false
     */
    private Map<Student,Boolean> makeProfessors(){
        professors = new HashMap<>();
        for (Student sc : Student.values()){
            professors.put(sc,false);
        }
        return professors;
    }

    @Override
    public String toString() {
        String dr = "Dining Room {\n";
        for (Student student : Student.values()){
            dr += student + ": ";
            dr += this.tables.get(student);
            dr += (this.professors.get(student)? " \u200D\uD83C\uDF93":"");
            dr += "\n";
        }
        dr += "}";
        return dr;
    }

    public Map<Student, Integer> getTables() {
        return tables;
    }

    public Map<Student, Boolean> getProfessors() {
        return professors;
    }
}
