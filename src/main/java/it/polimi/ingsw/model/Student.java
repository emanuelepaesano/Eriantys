package it.polimi.ingsw.model;

import java.util.*;
import java.util.HashMap;
// TODO: 11/04/2022
//  -> add abbreviations

public enum Student {
    YELLOW,
    BLUE,
    RED,
    PINK,
    GREEN,
    ;

    public static Map<Student, Integer> makeStudents() {
        Map<Student, Integer> studs = new HashMap<>();
        for (Student sc : Student.values()) {
            studs.put(sc, 0);
        }
        return studs;
    }

    public static Student askStudent(List<Student> students, Scanner scanner){
        String str;
        try{
            System.out.println("Choose a student color from the list :\n"
                    + students);
            str = scanner.nextLine();
            if (Objects.equals(str, "back")) {return null;}
            else {
                return Student.valueOf(str.toUpperCase());
            }
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Not a valid color, try again.");
            return (null);
        }
    }
}


