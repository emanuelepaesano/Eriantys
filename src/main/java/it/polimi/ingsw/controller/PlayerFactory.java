package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.Entrance;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class PlayerFactory {

    public Player makePlayer(int id, int numPlayers){
        String pname = askPlayerName(id);
        Map<Assistant, Boolean> assistants = buildDeck();
        DiningRoom diningRoom = assignDiningRoom();
        Entrance entrance = assignEntrance(numPlayers);

        return new Player(id, pname, assistants, (numPlayers==3? 4 : 3), (numPlayers == 3? 6 : 8), diningRoom, entrance);
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

    private DiningRoom assignDiningRoom() {
        return new DiningRoom();
    }

    private Entrance assignEntrance(int numplayers) {
        return new Entrance(numplayers);
    }
}
