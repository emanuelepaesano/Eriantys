package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.PlayerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
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
        PlayerFactory pf = new PlayerFactory();
        System.setIn(new ByteArrayInputStream("p1".getBytes(StandardCharsets.UTF_8)));
        p1 = pf.makePlayer(1,3);
        System.setIn(new ByteArrayInputStream("p2".getBytes(StandardCharsets.UTF_8)));
        p2 = pf.makePlayer(2,3);
        System.setIn(new ByteArrayInputStream("p3".getBytes(StandardCharsets.UTF_8)));
        p3 = pf.makePlayer(3,3);
        gameMap = new GameMap();
        gameMap.setMotherNature(1);
    }

//    check mothernature is updated
    @Test
    void moveMotherNatureAndCheck_checkMotherNaturePosition() {
//        setting scenario
        p1.setCurrentAssistant(Assistant.FIVE);
        gameMap.getArchipelago().get(2).setOwner(p1);
        gameMap.getArchipelago().get(3).setOwner(p2);
        gameMap.getArchipelago().get(4).setOwner(p1);

//        "p1, how many steps do you want to move Mother Nature? (At least 1, maximum 3)"
        System.setIn(new ByteArrayInputStream("2".getBytes(StandardCharsets.UTF_8)));
        gameMap.moveMotherNatureAndCheck(p1, List.of(p1, p2, p3));
//        1 (gameMap.setMotherNature(1);) + 2 (moves)
        assertEquals(3, gameMap.getMotherNature());
    }

    //    check archpelago is update for left, right, both
//    the owner of the island 3 changes from p2 to p1.
    @Test
    void moveMotherNatureAndCheck_checkOwnerUpdate() {
//        setting scenario
        p1.setCurrentAssistant(Assistant.FIVE);
        p1.getDiningRoom().getTables().putAll(
                Map.of(  Student.YELLOW, 999, Student.BLUE,999,  Student.PINK,999,  Student.GREEN,999,  Student.RED,999  ));
//        need checkProfessors() to update professors
        p1.getDiningRoom().checkProfessors(List.of(p1, p2, p3));

        System.out.println(p1.getDiningRoom().getProfessors().toString());
        System.out.println(p2.getDiningRoom().getProfessors().toString());
        gameMap.getArchipelago().get(3).setOwner(p2);
        gameMap.getArchipelago().get(3).setStudents(Map.of(
                Student.YELLOW, 999, Student.BLUE,999,  Student.PINK,999,  Student.GREEN,999,  Student.RED,999
        ));

//        "p1, how many steps do you want to move Mother Nature? (At least 1, maximum 3)"
        System.setIn(new ByteArrayInputStream("2".getBytes(StandardCharsets.UTF_8)));
        gameMap.moveMotherNatureAndCheck(p1, List.of(p1, p2, p3));
        System.out.println(gameMap.toString());
        assertEquals(p1, gameMap.getArchipelago().get(3).getOwner());
    }


    @Test
    void moveMotherNatureAndCheck_checkOwnerJoins() {
        //        setting scenario
        p1.setCurrentAssistant(Assistant.FIVE);
        p1.getDiningRoom().getTables().putAll(
                Map.of(  Student.YELLOW, 999, Student.BLUE,999,  Student.PINK,999,  Student.GREEN,999,  Student.RED,999  ));
//        need checkProfessors() to update professors
        p1.getDiningRoom().checkProfessors(List.of(p1, p2, p3));

        System.out.println(p1.getDiningRoom().getProfessors().toString());
        System.out.println(p2.getDiningRoom().getProfessors().toString());
        gameMap.getArchipelago().get(2).setOwner(p1);
        gameMap.getArchipelago().get(3).setOwner(p2);
        gameMap.getArchipelago().get(3).setStudents(Map.of(
                Student.YELLOW, 999, Student.BLUE,999,  Student.PINK,999,  Student.GREEN,999,  Student.RED,999
        ));
        gameMap.getArchipelago().get(4).setOwner(p1);

//        "p1, how many steps do you want to move Mother Nature? (At least 1, maximum 3)"
        System.setIn(new ByteArrayInputStream("2".getBytes(StandardCharsets.UTF_8)));
        gameMap.moveMotherNatureAndCheck(p1, List.of(p1, p2, p3));
        System.out.println(gameMap.toString());
        assertEquals(p1, gameMap.getArchipelago().get(3).getOwner());
    }

    @Test
    void testToString() {
    }

    @Test
    void getArchipelago() {
        assertInstanceOf(List.class, gameMap.getArchipelago());
        for (int i = 0; i < gameMap.getArchipelago().size(); i++) {
            assertInstanceOf(Island.class, gameMap.getArchipelago().get(i));
        }
    }

}