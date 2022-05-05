package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.*;

public class Entrance implements Serializable {

    private final List<Student> students;
    private final int size;

    public Entrance(int numPlayers){
        this.size = (numPlayers==3? 9:7);
        //initialize all entries to null
        students = new ArrayList<>(Arrays.asList(new Student[size]));
    }



    //only for test, will need to draw from the clouds/bag in the game
    public void fillTEST(){
        for (int i = 0; i< this.size;i++){
            Student s = Arrays.asList(Student.values()).get(i%5);
            students.set(i,s);
        }
    }
    @Override
    public String toString() {
        return "Entrance{" +
                "students: " + students +
                '}';
    }

    public List<Student> getStudents() {
        return students;
    }


}



