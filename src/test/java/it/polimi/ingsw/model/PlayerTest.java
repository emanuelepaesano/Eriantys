package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player testplayer;

    @BeforeEach
    void setUp(){
        System.setIn(new ByteArrayInputStream("player1".getBytes(StandardCharsets.UTF_8)));
        testplayer = Player.makePlayer(1,3);
    }

//    @ParameterizedTest (name = "Testing: {0}")
//    @ValueSource(ints = {1, 2, 3, 4})
//    void askWizard(Integer choice) {
//        System.setIn(new ByteArrayInputStream(choice.toString().getBytes()));
//        assertEquals(testplayer.askWizard(List.of(1,2,3,4)),choice);
//        assertEquals(choice, testplayer.getWizard());
//    }


//    static Stream<Arguments> giveRemainingsandChoice(){
//        //give all possible configurations of remaining colors and choices
//        }
//    }
    // TODO: 01/05/2022 move this to controller test
//    @ParameterizedTest (name = "Testing: {0}")
//    @EnumSource (TowerColor.class)
//    void askTowerColor(TowerColor choice) {
//        System.setIn(new ByteArrayInputStream(choice.toString().getBytes()));
//        assertEquals(testplayer.askTowerColor(List.of(TowerColor.values())),choice);
//        assertEquals(choice, testplayer.getTowerColor());
//    }



    @ParameterizedTest (name = "Testing: {0}")
    @EnumSource (Assistant.class)
    void playAssistant(Assistant assistant) {
        System.setIn(new ByteArrayInputStream(assistant.toString().getBytes()));
        Assistant choice = testplayer.playAssistant();
        assertEquals(choice, assistant); //test that we return the chosen assistant
        assertEquals(choice, testplayer.getCurrentAssistant()); //test that we set the choice as currentAssistant
        assertFalse(testplayer.getAssistants().get(choice)); //test that we set the boolean to false
        //test that we cannot play a false assistant. There might be a better way.
        System.setIn(new ByteArrayInputStream(assistant.toString().getBytes()));
        assertThrows(NoSuchElementException.class, ()->testplayer.playAssistant());

   }

    @Test
    void doActions() {
        //for this we need also to create a map, a diningroom and a entrance
    }

    @ParameterizedTest (name = "Testing for: Assistant {0}; {1} moves")
    @CsvSource({ "ONE, 1",
            "ONE, 2",
            "FIVE, 0",
            "FIVE, 3",
            "TEN, 6",
            "TEN, 4",
            "EIGHT, -3",
            "SEVEN, 2"
    })
    void askMNMoves(Assistant assistant, Integer triedMoves) {
        System.setIn(new ByteArrayInputStream(triedMoves.toString().getBytes()));
        //test that: works if the moves are acceptable (=returns the moves)
        //           does not work if not acceptable (=throws nosuchelement)
        testplayer.setCurrentAssistant(assistant);
        if (triedMoves <= assistant.getMoves() && triedMoves > 0){
            assertEquals(triedMoves,testplayer.askMNMoves());
        }
        else{assertThrows(NoSuchElementException.class, ()->testplayer.askMNMoves());}
    }



    @Test
    @DisplayName("Test of influence calculation on different islands")
    void calculateInfluence() {
        //we have red and green professor
        testplayer.getDiningRoom().getProfessors().putAll(Map.of(Student.GREEN,true,Student.RED,true));
        //not ours, size = 1
        Island island1 = new Island(1);
        island1.getStudents().putAll(Map.of(Student.GREEN,3,Student.BLUE,5));
        assertEquals(3,testplayer.calculateInfluence(island1));
        //ours, size = 1
        Island island2 = new Island(2);
        island2.setOwner(testplayer);
        island2.getStudents().putAll(Map.of(Student.RED,3,Student.YELLOW,5,Student.GREEN,1));
        assertEquals(5,testplayer.calculateInfluence(island2));
        //ours, size = 3
        Island island3 = new Island(3);
        island3.setSize(3);
        island3.setOwner(testplayer);
        island3.getStudents().putAll(Map.of(Student.PINK,6,Student.GREEN,3));
        assertEquals(6,testplayer.calculateInfluence(island3));
    }
}