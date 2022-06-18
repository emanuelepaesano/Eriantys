package it.polimi.ingsw.model;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.ActionPhaseMessage;
import it.polimi.ingsw.messages.PickStudMessage;

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

    // TODO: 17/06/2022 We cannot keep the catch like this, but idk what was done with the characters.
    public static String askStudent(List<Student> students, VirtualView user, String whereFrom) {
        String str = "";
        try{
            new PickStudMessage(students, whereFrom).send(user);
            try {
                str = user.getReply();
            }catch (DisconnectedException ex){}
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

    public static String askStudent(Player player, VirtualView user) throws DisconnectedException {
        String str;
        try{
            new ActionPhaseMessage(player, studselect).sendAndCheck(user);
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


