package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static it.polimi.ingsw.model.Student.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class EntranceTest {
    private Entrance testEntrance;
    private DiningRoom testDiningRoom;
    private GameMap testGameMap;

    @BeforeEach
    void setUp(){
        testEntrance= new Entrance(3);
        testEntrance.fillTEST();
        System.out.println(testEntrance.getStudents());
        testDiningRoom = new DiningRoom();
        testGameMap = new GameMap();
    }

    private static Stream<Arguments> sourceforMovetoDR(){
        InputStream i0 = new ByteArrayInputStream("0\n".getBytes());
        InputStream i1 = new ByteArrayInputStream("back\n".getBytes());
        InputStream i2 = new ByteArrayInputStream("1\nred".getBytes());
        InputStream i3 = new ByteArrayInputStream("3\npink\nred\nyellow".getBytes());
        InputStream i4 = new ByteArrayInputStream("3\npink\npink\npink".getBytes());
        InputStream ierr1 = new ByteArrayInputStream("three\nred".getBytes());
        InputStream ierr2 = new ByteArrayInputStream("3\nyelllowww".getBytes());
        return Stream.of(arguments(i0,"0.0"),arguments(i1,"0.1"),arguments(i2,"1"),
                arguments(i3,"3"),arguments(i4,"4"),arguments(ierr1,"err1"),arguments(ierr2,"err2"));
    }

    /**
     * For all of these tests it is assumed that the player can still move all of his students (availableMoves = 4 or 3).<br>
     * Initial entrance, from entrance.fillTEST() : [YELLOW, BLUE, RED, PINK, GREEN, YELLOW, BLUE, RED, PINK]
     * @param inputs the user input required for each test case.
     * @param testCase Test cases considered:<br>
     *                 (testCase 0.0) -> the player moves 0 students. In this case we do not ask anything else and return 0<br>
     *                 (testCase 0.1) -> the player types "back". The same as testCase 0 should happen.<br>
     *                 (testCase 1) -> the player moves 1 student (RED).<br>
     *                 (testCase 3) -> the player moves 3 students (PINK,RED,YELLOW).<br>
     *                 (testCase 4) -> the player moves 3 students (PINK,PINK,PINK), but only has 2 of them. <br>
     *                 In this case the diningroom should be updated with 2 pinks, but the method will look<br> for a new line
     *                 which is not there.<br>
     *                 (testCase err1/err2) -> 2 error cases in which the player types wrong input
     */
    @ParameterizedTest (name = "Testing testCase {1} ...")
    @MethodSource("sourceforMovetoDR")
    void moveToDiningRoom(InputStream inputs, String testCase) {
        System.setIn(inputs);
        if (testCase == "0.0"){
            assertEquals(0,testEntrance.moveToDiningRoom(4,testDiningRoom),
                    "We should not move students, nor ask anything");
        }
        if (testCase =="0.1"){
            assertEquals(0,testEntrance.moveToDiningRoom(4,testDiningRoom),
                    "We should not move students, nor ask anything");
        }
        else if (testCase =="1") { //1 red student
            assertEquals(1, testEntrance.moveToDiningRoom(4, testDiningRoom),
                    "We should move exactly 1 student");
            assertEquals(List.of(YELLOW,BLUE,PINK,GREEN,YELLOW,BLUE,RED,PINK),testEntrance.getStudents(),
                    "We should have removed the first red");
            assertEquals(Map.of(GREEN,0,YELLOW,0,PINK,0,BLUE,0,RED,1), testDiningRoom.getTables(),
                    "The dinigRoom should have only 1 red now");
        }
        else if (testCase =="3"){ //3 students: pink,red,yellow
            assertEquals(3,testEntrance.moveToDiningRoom(4,testDiningRoom));
            assertEquals(List.of(BLUE,GREEN,YELLOW,BLUE,RED,PINK),testEntrance.getStudents(),
                    "We should have removed the first yellow,first pink and first red");
            assertEquals(Map.of(GREEN,0,YELLOW,1,PINK,1,BLUE,0,RED,1), testDiningRoom.getTables(),
                    "The dinigRoom should have exactly 1 yellow, 1 pink and 1 red");
        }
        else if (testCase =="4"){ //3 students: pink,pink,pink. But we only have 2 pinks
            assertThrows(NoSuchElementException.class, ()->testEntrance.moveToDiningRoom(4,testDiningRoom),
                    "The method should not find a new line");
            assertEquals(List.of(YELLOW,BLUE,RED,GREEN,YELLOW,BLUE,RED),testEntrance.getStudents(),
                    "We should have removed the 2 pinks");
            assertEquals(Map.of(GREEN,0,YELLOW,0,PINK,2,BLUE,0,RED,0), testDiningRoom.getTables(),
                    "The dinigRoom should have exactly 2 pinks");
        }
        else if (testCase =="err1" || testCase=="err2"){ //should retry, but no new line found
            assertThrows(NoSuchElementException.class, ()->testEntrance.moveToDiningRoom(4,testDiningRoom) );
        }
    }

    @Test
    void moveToIsland() {
    }
}