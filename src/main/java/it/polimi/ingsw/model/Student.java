package it.polimi.ingsw.model;

import java.util.EnumMap;
import java.util.Map;


public enum Student {
    YELLOW,
    BLUE,
    RED,
    PINK,
    GREEN,
    ;

    public static Map<Student, Integer> makeStudents() {
        Map<Student, Integer> studs = new EnumMap<>(Student.class);
        for (Student sc : Student.values()) {
            studs.put(sc, 0);
        }
        return studs;
    }

    public String getAnsiColor(){
        switch (this){
            case YELLOW -> {return Game.ANSI_YELLOW;}
            case PINK -> {return Game.ANSI_PINK;}
            case GREEN -> {return Game.ANSI_GREEN;}
            case RED -> {return Game.ANSI_RED;}
            case BLUE -> {return Game.ANSI_BLUE;}
            default -> {return "";}
        }
    }

    @Override
    public String toString() {
        return this.getAnsiColor()+ this.name() + Game.ANSI_RESET;
    }
}


