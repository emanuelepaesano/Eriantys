package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiningRoom {

    private Map<Student, Integer> tables;
    private Map<Student, Boolean> professors;


    public DiningRoom(){
        this.tables = Student.makeStudents();
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


    public void checkProfessors(List<Player> players){
        //look into all players to see if we get that professor (for every table)
        //N.B: we win only with strictly more students
        for (Student table : Student.values()) {
            int countwins = 0;
            for (Player p : players) {
                if (this.tables.get(table) > p.getDiningRoom().tables.get(table)) {
                        countwins += 1;
                }
            }
            if (countwins == (players.size())-1) {
                //set all to false and then our to true
                for (Player p : players) {
                    p.getDiningRoom().professors.replace(table, true, false);
                }
                this.professors.replace(table, true);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder dr = new StringBuilder("Dining Room {\n");
        for (Student student : Student.values()){
            dr.append(student).append(": ").append(this.tables.get(student));
            dr.append(this.professors.get(student) ? " \u200D\uD83C\uDF93" : "").append("\n");
        }
        dr.append("}");
        return dr.toString();
    }

    public Map<Student, Integer> getTables() {
        return tables;
    }

    public Map<Student, Boolean> getProfessors() {
        return professors;
    }
}
