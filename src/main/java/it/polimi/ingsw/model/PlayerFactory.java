package it.polimi.ingsw.model;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class PlayerFactory {

    public Player makePlayer(int id, int numPlayers){
        String pname = askPlayerName(id);
        Map<Assistant, Boolean> assistants = buildDeck();
        School school = assignSchool(numPlayers);
        return new Player(id, pname, assistants, (numPlayers==3? 4 : 3), (numPlayers == 3? 6 : 8), school);
    }

    private String askPlayerName(int id) {
        System.out.println("Player " + id + ", enter your nickname:");
        return (new Scanner(System.in).nextLine());
    }

    private Map<Assistant, Boolean> buildDeck(){
        Map<Assistant,Boolean> tm = new TreeMap<>();
        for (Assistant as : Assistant.values()) {
            tm.put(as, true);
        }
        return tm;
    }

    private School assignSchool(int numplayers){
        return new School(numplayers);
    }
}
