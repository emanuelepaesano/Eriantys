package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PlayerTest {
    private Player testplayer;

    @BeforeEach
    void setUp(){
        System.setIn(new ByteArrayInputStream("player1".getBytes(StandardCharsets.UTF_8)));
        testplayer = new Player(1,3);
    }

    @ParameterizedTest (name = "Testing: {0}")
    @ValueSource(ints = {1, 2, 3, 4})
    void askWizard(Integer choice) {
        System.setIn(new ByteArrayInputStream(choice.toString().getBytes()));
        assertEquals(testplayer.askWizard(List.of(1,2,3,4)),choice);
    }


//    static Stream<Arguments> giveRemainingsandChoice(){
//        //give all possible configurations of remaining colors and choices
//        }
//    }
    @ParameterizedTest (name = "Testing: {0}")
    @EnumSource (TowerColor.class)
    void askTowerColor(TowerColor choice) {
        System.setIn(new ByteArrayInputStream(choice.toString().getBytes()));
        assertEquals(testplayer.askTowerColor(List.of(TowerColor.values())),choice);

    }



    @ParameterizedTest (name = "Testing: {0}")
    @EnumSource (Assistant.class)
    void playAssistant(Assistant assistant) {
        System.setIn(new ByteArrayInputStream(assistant.toString().getBytes()));
        Assistant choice = testplayer.playAssistant();
        assertEquals(choice, assistant); //test that we return the chosen assistant
        assertEquals(choice, testplayer.getCurrentAssistant()); //test that we set the choice as currentAssistant
        //we are not testing yet that we are setting the bool to false and that the false assistants cannot be played.
        //to do this we might try a assertTimeout on the input or something
   }

    @Test
    void doActions() {
        //for this we need also to create a map, a diningroom and a entrance
    }

    @Test
    void askMNMoves() {
    }


    @Test
    void calculateInfluence() {
        //we have red and green professor
        testplayer.getDiningRoom().getProfessors().putAll(Map.of(Student.GREEN,true,Student.RED,true));
        //not ours, size = 1
        Island island1 = new Island(1);
        island1.getStudents().putAll(Map.of(Student.GREEN,3,Student.BLUE,5));
        assertEquals( 3,testplayer.calculateInfluence(island1));
        //ours, size = 1
        Island island2 = new Island(2);
        island2.setOwner(testplayer);
        island2.getStudents().putAll(Map.of(Student.RED,3,Student.YELLOW,5,Student.GREEN,1));
        assertEquals( 5,testplayer.calculateInfluence(island2));
        //ours, size = 3
        Island island3 = new Island(3);
        island3.setSize(3);
        island3.setOwner(testplayer);
        island3.getStudents().putAll(Map.of(Student.PINK,6,Student.GREEN,3));
        assertEquals( 6,testplayer.calculateInfluence(island3));


    }
}