package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class DiningRoom implements Serializable {

    private Map<Student, Integer> tables;
    private Map<Student, Boolean> professors;

    private Integer coins;


    public DiningRoom(){
        this.tables = Student.makeStudents();
        this.professors= makeProfessors();
        coins = null; //the game will set this to 1 later in case of advanced variant
    }


    public void putStudent(Student student){
        int oldnum = this.tables.get(student);
        int newnum = oldnum + 1;
        if (newnum > 10) {
            System.err.println("Your diningRoom is full!");
            return;}
        if (newnum > 0 && newnum%3==0){ if(coins!=null) {coins+=1;} }
        this.tables.replace(student,oldnum,newnum);
    }

    /**
     *
     * @return initializes all professors to false
     */
    private Map<Student,Boolean> makeProfessors(){
        professors = new EnumMap<>(Student.class);
        for (Student sc : Student.values()){
            professors.put(sc,false);
        }
        return professors;
    }


    public void checkProfessors(List<Player> players, Boolean orEqual) {
        //look into all players to see if we get that professor (for every table)
        //N.B: we win only with strictly more students
        for (Student table : Student.values()) {
            int countwins;
            if (orEqual) {countwins= countOrEqualWins(players, table);}
            else {countwins = countNormalWins(players,table);}
            //you need enough wins, but with orEqual you will also beat yourself
            if (orEqual? countwins == (players.size()) : countwins == (players.size()) - 1) {
                for (Player p : players) {
                    p.getDiningRoom().professors.replace(table, true, false);
                }
                this.professors.replace(table, true);
            }
        }
    }

    private int countNormalWins(List<Player> players, Student table){
        int countwins = 0;
        for (Player p : players) {
                if (this.tables.get(table) > p.getDiningRoom().tables.get(table)) {
                    countwins += 1;
                }
        }
        return countwins;

    }
    private int countOrEqualWins(List<Player> players, Student table){
        int countwins = 0;
        for (Player p : players) {
            if (this.tables.get(table) >= p.getDiningRoom().tables.get(table)) {
                countwins += 1;
            }
        }
        return countwins;
    }

    @Override
    public String toString() {
        StringBuilder dr = new StringBuilder("Dining Room {\n");
        for (Student student : Student.values()){
            dr.append(student).append(": ");
            dr.append(tables.get(student));
            dr.append(this.professors.get(student) ? student.getAnsiColor()+" \u200D\uD83C\uDF93"+Game.ANSI_RESET : "").append("\n");
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

    public void setProfessors(Map<Student, Boolean> professors) {
        this.professors = professors;
    }
    int getCoins() { return coins;}

    void setCoins(int coins) {this.coins = coins;}
}
