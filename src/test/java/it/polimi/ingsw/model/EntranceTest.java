package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.stream.Stream;

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

    private static Stream<Arguments> sourceForMovetoDR(){
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

//    /**
//     * It is assumed that the player can still move all of his students (availableMoves = 4 or 3).<br>
//     * Initial entrance, from entrance.fillTEST() : [YELLOW, BLUE, RED, PINK, GREEN, YELLOW, BLUE, RED, PINK]
//     * @param testCase Test cases considered:<br>
//     *                 (testCase 0.0) -> the player moves 0 students. In this case we do not ask anything else and return 0<br>
//     *                 (testCase 0.1) -> the player types "back". The same as testCase 0 should happen.<br>
//     *                 (testCase 1) -> the player moves 1 student (RED).<br>
//     *                 (testCase 3) -> the player moves 3 students (PINK,RED,YELLOW).<br>
//     *                 (testCase 4) -> the player moves 3 students (PINK,PINK,PINK), but only has 2 of them. <br>
//     *                 (testCase err1/err2) -> 2 error cases in which the player types wrong input
//     * @param inputs the user input required to test each case.
//     */
//    @ParameterizedTest (name = "Testing testCase {1} ...")
//    @MethodSource("sourceForMovetoDR")
//    void moveToDiningRoom(InputStream inputs, String testCase) {
//        System.setIn(inputs);
//        switch (testCase) {
//            case "0.0", "0.1" -> assertEquals(0, testEntrance.moveToDiningRoom(4, testDiningRoom),
//                    "We should not move students, nor ask anything");
//            case "1" -> { //1 red student
//                assertEquals(1, testEntrance.moveToDiningRoom(4, testDiningRoom),
//                        "We should move exactly 1 student");
//                assertEquals(List.of(YELLOW, BLUE, PINK, GREEN, YELLOW, BLUE, RED, PINK), testEntrance.getStudents(),
//                        "We should have removed the first red");
//                assertEquals(Map.of(GREEN, 0, YELLOW, 0, PINK, 0, BLUE, 0, RED, 1), testDiningRoom.getTables(),
//                        "The diningRoom should have only 1 red now"); }
//            case "3" -> { //3 students: pink,red,yellow
//                assertEquals(3, testEntrance.moveToDiningRoom(4, testDiningRoom));
//                assertEquals(List.of(BLUE, GREEN, YELLOW, BLUE, RED, PINK), testEntrance.getStudents(),
//                        "We should have removed the first yellow,first pink and first red");
//                assertEquals(Map.of(GREEN, 0, YELLOW, 1, PINK, 1, BLUE, 0, RED, 1), testDiningRoom.getTables(),
//                        "The diningRoom should have exactly 1 yellow, 1 pink and 1 red"); }
//            case "4" -> { //3 students: pink,pink,pink. But we only have 2 pinks
//                assertThrows(NoSuchElementException.class, () -> testEntrance.moveToDiningRoom(4, testDiningRoom),
//                        "The method should not find a new line");
//                assertEquals(List.of(YELLOW, BLUE, RED, GREEN, YELLOW, BLUE, RED), testEntrance.getStudents(),
//                        "We should have removed the 2 pinks");
//                assertEquals(Map.of(GREEN, 0, YELLOW, 0, PINK, 2, BLUE, 0, RED, 0), testDiningRoom.getTables(),
//                        "The diningRoom should have exactly 2 pinks"); }
//            case "err1", "err2" ->
//                    assertThrows(NoSuchElementException.class, () -> testEntrance.moveToDiningRoom(4, testDiningRoom));
//        }
//    }


    private static Stream<Arguments> sourceForMoveToIsland(){
        InputStream i2 = new ByteArrayInputStream("1\nred\n10".getBytes());
        InputStream i3 = new ByteArrayInputStream("3\npink\n10\nred\n10\nyellow\n3".getBytes());
        InputStream i4 = new ByteArrayInputStream("3\npink\n10\npink\n10\npink".getBytes());
        InputStream ierr1 = new ByteArrayInputStream("three\nred\n18".getBytes());
        InputStream ierr2 = new ByteArrayInputStream("3\nyellow\nonehundred".getBytes());
        return Stream.of(arguments(i2,"1"),
                arguments(i3,"3"),arguments(i4,"4"),arguments(ierr1,"err1"),arguments(ierr2,"err2"));

    }
    /**
     * Similar configuration of MovetoDiningRoom(). The 0 cases are the same, so we will not test them
     * @param testCase Test cases considered:<br>
     *                 (testCase 1) -> the player moves 1 student (RED) to island 10.<br>
     *                 (testCase 3) -> the player moves 2 students (PINK,RED) to island 10 and 1 (YELLOW) to island 3.<br>
     *                 (testCase 4) -> the player wants to move 3 students (PINK,PINK,PINK) to island 10, but only has 2. <br>
     *                 (testCase err1/err2) -> 2 the player types a wrong island number
     */
//    @ParameterizedTest (name = "Testing case {1} ...")
//    @MethodSource("sourceForMoveToIsland")
//    void moveToIsland(InputStream inputs, String testCase) {
//        //we test movement of students from a testEntrance to a testIsland.
//        //The error and 0 cases are the same as moveToDiningRoom. so we will not test them.
//        //We need to test that, in the successful cases, the students are moved to the correct island
//        Island testIsland = testGameMap.getArchipelago().get(10);
//        Island island3 = testGameMap.getArchipelago().get(3);
//        Map<Student,Integer> islandStudents = testIsland.getStudents();
//        System.setIn(inputs);
//        switch (testCase) {
//            case "1" -> {
//                //1 red student to island 10
//                assertEquals(1, testEntrance.moveToIsland(4, testGameMap),
//                        "We should move exactly 1 student");
//                assertEquals(List.of(YELLOW, BLUE, PINK, GREEN, YELLOW, BLUE, RED, PINK), testEntrance.getStudents(),
//                        "We should have removed the first red");
//                assertEquals(Map.of(RED,1,BLUE,0,GREEN,0,YELLOW,0,PINK,0),islandStudents,
//                        "The Island should contain only 1 red now");
//            }
//            case "3" -> {
//                //3 students: pink,red to island 10, yellow to island 3
//                assertEquals(3, testEntrance.moveToIsland(4, testGameMap),
//                        "We should move 3 students");
//                assertEquals(List.of(BLUE, GREEN, YELLOW, BLUE, RED, PINK), testEntrance.getStudents(),
//                        "We should have removed the first yellow,first pink and first red");
//                assertEquals(Map.of(GREEN, 0, YELLOW, 0, PINK, 1, BLUE, 0, RED, 1), islandStudents,
//                        "Island 10 should have exactly 1 pink and 1 red");
//                assertEquals(Map.of(GREEN, 0, YELLOW, 1, PINK, 0, BLUE, 0, RED, 0), island3.getStudents(),
//                        "Island 3 should have exactly 1 yellow");
//            }
//            case "4" -> {
//                //3 students: pink,pink,pink. But we only have 2 pinks
//                assertThrows(NoSuchElementException.class, () -> testEntrance.moveToIsland(4, testGameMap),
//                        "The method should not find a new line");
//                assertEquals(List.of(YELLOW, BLUE, RED, GREEN, YELLOW, BLUE, RED), testEntrance.getStudents(),
//                        "We should have removed the 2 pinks");
//                assertEquals(Map.of(GREEN, 0, YELLOW, 0, PINK, 2, BLUE, 0, RED, 0), islandStudents,
//                        "Island 10 should have exactly 2 pinks"); }
//
//            case "err1", "err2" ->
//                    assertThrows(NoSuchElementException.class, () -> testEntrance.moveToIsland(4, testGameMap));
//        }
//   }
}