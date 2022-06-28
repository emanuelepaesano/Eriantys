package it.polimi.ingsw.model;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.ActionPhaseMessage;
import it.polimi.ingsw.messages.PickStudMessage;
import it.polimi.ingsw.messages.PlayCharMessage;

import java.util.*;

import static it.polimi.ingsw.messages.ActionPhaseMessage.ActionPhaseType.studselect;
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
    public static String askStudent(List<Student> students, VirtualView user, int indexChar){
        //99 is a special index only for when you have to move from the dining room
        String str;
        try{
            new PlayCharMessage(students, indexChar).send(user);
            str = user.getReply();
            if (Objects.equals(str, "back")) {return "back";}
            else {
                Student.valueOf(str.toUpperCase());
                return str;
            }
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Not a valid color, try again.");
            return "retry";
        }

    }

    public static String askStudent(Player player, VirtualView user){
        String str;
        try{
            new ActionPhaseMessage(player).send(user);
            str = user.getReply();
            if (Objects.equals(str, "back")) {return "back";}
            else {
                Student.valueOf(str.toUpperCase());
                return str;
            }
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Not a valid color, try again.");
            return "retry";
        }

    }
    @Override
    public String toString() {
        return this.getAnsiColor()+ this.name() + Game.ANSI_RESET;
    }
}


