package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameMapTest {
    private Player p1;
    private Player p2;
    private Player p3;
    private GameMap gameMap;

    /**
     * Set up
     */
    @BeforeEach
    void setUp(){
        p1 = Player.makePlayer(1,3);
        p2 = Player.makePlayer(2,3);
        p3 = Player.makePlayer(3,3);
        gameMap = new GameMap();
        gameMap.setMotherNature(1);
    }

    /**
     * check if the mother nature is assigned to an island.
     * check if the number of students assigned to each island is 0 or 1.
     */
    @Test
    void startMNAndStudents() {
        gameMap.startMNAndStudents();

        //  check if the mother nature is assigned to an island
        int motherNaturePosition = gameMap.getMotherNature();
        assertTrue(1 <= motherNaturePosition && motherNaturePosition <= 12);

        //  the student
        for (int i=1;i<12;i++) {
            gameMap.getArchipelago().get(i).getStudents().values().forEach(stuNum -> {
                assertTrue(stuNum == 0 || stuNum == 1);
            });
        }
    }

    /**
     * Check if the motherNature position is updated
     *
     */
    @Test
    void moveMotherNatureAndCheck() {
//        setting scenario
        p1.setCurrentAssistant(Assistant.FIVE);
        gameMap.getIslandById(2).setOwner(p1);
        gameMap.getIslandById(3).setOwner(p2);
        gameMap.getIslandById(4).setOwner(p1);

//        "p1, how many steps do you want to move Mother Nature? (At least 1, maximum 3)"
        gameMap.moveMotherNatureAndCheck(List.of(p1, p2, p3), 2);
//        1 (gameMap.setMotherNature(1);) + 2 (moves)
        assertEquals(3, gameMap.getMotherNature());
    }

    /**
     * The owner of the island 3 changes from p2 to p1.
     */
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
        gameMap.moveMotherNatureAndCheck(List.of(p1, p2, p3), 2);
        System.out.println(gameMap.toString());
        assertEquals(p1, gameMap.getIslandById(3).getOwner());
    }

    /**
     * Check if the archpelago joins the islands "both"
     */
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
        gameMap.moveMotherNatureAndCheck(List.of(p1, p2, p3), 2);
        System.out.println(gameMap.toString());

        Island expectedIsland = new Island(3);
        expectedIsland.setOwner(p1);
        expectedIsland.setStudents(expectedStudetns);
        assertEquals(expectedIsland.getOwner(), gameMap.getIslandById(3).getOwner());
        assertEquals(expectedIsland.getStudents(), gameMap.getIslandById(3).getStudents());
    }

    @Test
    void getArchipelago() {
        assertInstanceOf(List.class, gameMap.getArchipelago());
        for (int i = 0; i < gameMap.getArchipelago().size(); i++) {
            assertInstanceOf(Island.class, gameMap.getArchipelago().get(i));
        }
    }


//    HELPER

    /**
     * All the students are set to 99.
     * This method is used to set many students to the island.
     *
     * @return many students
     */
     Map<Student, Integer> makeSuperStudents() {
        Map<Student, Integer> studetns = new HashMap<>();
        for (Student sc : Student.values()) {
            studetns.put(sc, 99);
        }
        return studetns;
    }

    /**
     * All the professors are set to true.
     * This method is used to set all the professors to the Dining Table.
     *
     * @return professors
     */
    Map<Student, Boolean> makeSuperProfessors() {
        Map<Student, Boolean> professors = new HashMap<>();
        for (Student sc : Student.values()) {
            professors.put(sc, true);
        }
        return professors;
    }


}