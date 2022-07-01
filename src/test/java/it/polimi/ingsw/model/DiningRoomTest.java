package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DiningRoomTest {
    DiningRoom testDiningRoom;
    @BeforeEach
    void setUp(){
        testDiningRoom = new DiningRoom();

    }

    private static Stream<Arguments> givePlayers(){
        System.setIn(new ByteArrayInputStream("player1".getBytes(StandardCharsets.UTF_8)));
        Player player1 = Player.makePlayer(1,3);
        player1.getDiningRoom().getTables().putAll(Map.of(Student.BLUE,3,Student.YELLOW,5));

        System.setIn(new ByteArrayInputStream("player2".getBytes(StandardCharsets.UTF_8)));
        Player player2 = Player.makePlayer(2,3);
        player2.getDiningRoom().getTables().putAll(Map.of(Student.RED,3,Student.PINK,1,Student.GREEN,1));

        System.setIn(new ByteArrayInputStream("player3".getBytes(StandardCharsets.UTF_8)));
        Player player3 = Player.makePlayer(3,3);
        player3.getDiningRoom().getTables().putAll(Map.of(Student.PINK,1,Student.GREEN,3));

        //this simulates the owner of this dinigroom: it is a player you can never beat
        System.setIn(new ByteArrayInputStream("Me".getBytes(StandardCharsets.UTF_8)));
        Player player4 = Player.makePlayer(0,3);
        player4.getDiningRoom().getTables().putAll(Map.of(Student.RED,10,Student.GREEN,10,
                Student.YELLOW,10,Student.BLUE,10,Student.PINK,10));

        return Stream.of(arguments(List.of(player1,player2,player3,player4),Map.of(Student.RED,false,Student.GREEN,true,
                Student.YELLOW,false,Student.BLUE, false,Student.PINK,true)));
    }

    /**
     *Test cases: <br> BLUE -> We have same students of best opponent. We don't get that professor;<br>
     * YELLOW,RED -> lower than best opponent, no professor;<br>PINK,GREEN -> More than best opponent, we get the professor
     */
    @ParameterizedTest (name = "Testing change of professors ...")
    @MethodSource("givePlayers")
    void checkProfessors(List<Player> players, Map<Student,Boolean> expected) {
        //best opponent's student number for: BLUE=2, PINK=1, GREEN=3, YELLOW=5, RED=3
        testDiningRoom.getTables().putAll(
        Map.of(  Student.BLUE,3,  Student.PINK,9,  Student.GREEN,4,  Student.RED,1  ));
        testDiningRoom.checkAllProfessors(players,false);
        assertEquals(expected,testDiningRoom.getProfessors(),"We should get only pink and green professors");
    }

    @Test
    @DisplayName("Test correct initialization of professors")
    void getProfessors() {
        //this is to test makeProfessors
        Map<Student,Boolean> expected = Map.of(Student.BLUE,false,Student.GREEN,false,
                Student.RED,false,Student.YELLOW,false, Student.PINK, false);

        assertEquals(expected, testDiningRoom.getProfessors());
    }

    @Test
    @DisplayName("Test the correct placement of a student")
    void putStudent(){
        Student student = Student.GREEN;
        testDiningRoom.putStudent(student);

        Map<Student,Integer> expected = Map.of(Student.BLUE,0,Student.GREEN,1,
                Student.RED,0,Student.YELLOW,0, Student.PINK, 0);

        assertEquals(expected, testDiningRoom.getTables());
    }

    @Test
    @DisplayName("Test the correct placement of a student, when the dining room is already full")
    void putStudentFull(){
        Student student = Student.GREEN;
        testDiningRoom.getTables().putAll(
                Map.of(  Student.BLUE,3,  Student.PINK,3,  Student.GREEN,10,  Student.RED,3, Student.YELLOW,3  ));
        testDiningRoom.putStudent(student);
        Map<Student,Integer> expected = Map.of(Student.BLUE,3,Student.GREEN,10,
                Student.RED,3,Student.YELLOW,3, Student.PINK, 3);

        assertEquals(expected, testDiningRoom.getTables());
    }

}