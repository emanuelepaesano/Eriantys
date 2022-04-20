package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class DiningRoom {
    // TODO: 12/04/2022 reference to player
    //  we may not need it for now
    private Map<Student, Integer> tables;
    private Map<Student, Boolean> professors;
    private final Player player;

    //maybe the entrance can contain a reference to the dining room
    //so we dont pass through player every time. then we make the dining room first
    public DiningRoom(Player player){
        this.player= player;
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


    public void checkProfessor(Student student){
        //look into all players to see if we get that professor
        //N.B: we win only with strictly more students
        int countwins = 0;
        for (Player p : player.getGame().getTableOrder()){
            if (p.id != this.player.id){
                if (this.tables.get(student) > p.getDiningRoom().tables.get(student)){
                    countwins+=1;
                }
            }
        }
        if (countwins == (player.getNumPlayers()-1)){
            //set all to false and then our to true
            for (Player p : player.getGame().getTableOrder()){
                p.getDiningRoom().professors.replace(student,true,false);
            }
            this.professors.replace(student,true);
        }
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
