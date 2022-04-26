package it.polimi.ingsw.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {
    private Player p1;
    private Player p2;
    private Player p3;
    private GameMap gameMap;

    @BeforeEach
    void setUp(){
        System.setIn(new ByteArrayInputStream("p1".getBytes(StandardCharsets.UTF_8)));
        p1 = Player.makePlayer(1,3);
        System.setIn(new ByteArrayInputStream("p2".getBytes(StandardCharsets.UTF_8)));
        p2 = Player.makePlayer(2,3);
        System.setIn(new ByteArrayInputStream("p3".getBytes(StandardCharsets.UTF_8)));
        p3 = Player.makePlayer(3,3);
        gameMap = new GameMap();
        gameMap.setMotherNature(1);
    }

//    check if the motherNature position is updated
    @Test
    void moveMotherNatureAndCheck_checkMotherNaturePosition() {
//        setting scenario
        p1.setCurrentAssistant(Assistant.FIVE);
        gameMap.getIslandById(2).setOwner(p1);
        gameMap.getIslandById(3).setOwner(p2);
        gameMap.getIslandById(4).setOwner(p1);

//        "p1, how many steps do you want to move Mother Nature? (At least 1, maximum 3)"
        System.setIn(new ByteArrayInputStream("2".getBytes(StandardCharsets.UTF_8)));
        gameMap.moveMotherNatureAndCheck(p1, List.of(p1, p2, p3));
//        1 (gameMap.setMotherNature(1);) + 2 (moves)
        assertEquals(3, gameMap.getMotherNature());
    }

//    the owner of the island 3 changes from p2 to p1.
    @Test
    void moveMotherNatureAndCheck_checkOwnerUpdate() {
//        setting scenario
        p1.setCurrentAssistant(Assistant.FIVE);
        p1.getDiningRoom().setProfessors(makeSuperProfessors());

        System.out.println(p1.getDiningRoom().getProfessors().toString());
        System.out.println(p2.getDiningRoom().getProfessors().toString());
        gameMap.getIslandById(3).setOwner(p2);
        gameMap.getIslandById(3).setStudents(makeSuperStudents());

//        "p1, how many steps do you want to move Mother Nature? (At least 1, maximum 3)"
        System.setIn(new ByteArrayInputStream("2".getBytes(StandardCharsets.UTF_8)));
        gameMap.moveMotherNatureAndCheck(p1, List.of(p1, p2, p3));
        System.out.println(gameMap.toString());
        assertEquals(p1, gameMap.getIslandById(3).getOwner());
    }

//    check if the archpelago joins the islands "both"
    @Test
    void moveMotherNatureAndCheck_checkOwnerJoins() {
        //        setting scenario
        p1.setCurrentAssistant(Assistant.FIVE);
        p1.getDiningRoom().setProfessors(makeSuperProfessors());

        gameMap.getIslandById(2).setOwner(p1);
        gameMap.getIslandById(3).setOwner(p2);
        Map<Student, Integer> studetns = makeSuperStudents();
        gameMap.getIslandById(3).setStudents(studetns);
        gameMap.getIslandById(4).setOwner(p1);

        Map<Student, Integer> expectedStudetns = makeSuperStudents();
        expectedStudetns.replaceAll((k,v) -> v + gameMap.getIslandById(2).getStudents().get(k));
        expectedStudetns.replaceAll((k,v) -> v + gameMap.getIslandById(4).getStudents().get(k));

//        "p1, how many steps do you want to move Mother Nature? (At least 1, maximum 3)"
        System.setIn(new ByteArrayInputStream("2".getBytes(StandardCharsets.UTF_8)));
        gameMap.moveMotherNatureAndCheck(p1, List.of(p1, p2, p3));
        System.out.println(gameMap.toString());

        Island expectedIsland = new Island(3);
        expectedIsland.setOwner(p1);
        expectedIsland.setStudents(expectedStudetns);
        assertEquals(expectedIsland.getOwner(), gameMap.getIslandById(3).getOwner());
        assertEquals(expectedIsland.getStudents(), gameMap.getIslandById(3).getStudents());
    }

    Map<Student, Integer> makeSuperStudents() {
        Map<Student, Integer> studetns = new HashMap<>();
        for (Student sc : Student.values()) {
            studetns.put(sc, 99);
        }
        return studetns;
    }

    Map<Student, Boolean> makeSuperProfessors() {
        Map<Student, Boolean> professors = new HashMap<>();
        for (Student sc : Student.values()) {
            professors.put(sc, true);
        }
        return professors;
    }

    @Test
    void getArchipelago() {
        assertInstanceOf(List.class, gameMap.getArchipelago());
        for (int i = 0; i < gameMap.getArchipelago().size(); i++) {
            assertInstanceOf(Island.class, gameMap.getArchipelago().get(i));
        }
    }

}