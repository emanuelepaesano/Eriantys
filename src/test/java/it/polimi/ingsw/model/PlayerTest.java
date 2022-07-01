package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {
    private Player testplayer;

    @BeforeEach
    void setUp(){
        System.setIn(new ByteArrayInputStream("player1".getBytes(StandardCharsets.UTF_8)));
        testplayer = Player.makePlayer(1,3);
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