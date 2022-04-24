package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class IslandTest {
    Island testIsland;

    @BeforeEach
    void setUp(){
        testIsland = new Island(1);
    }

    /**
     * @param testCase we are testing 3 main cases:<br>(testCase 1) -> the owner changes. In this case, check that the change is done correctly<br>
     *         (testCase 2) -> the owner does not change because no one has influence > owner<br>
     *         (testCase 3) -> the owner does not change because 2 or more players tie, even if they beat the owner
     */
    @ParameterizedTest(name = "Testing test case {1} ...")
    @MethodSource("sourceForCheckOwner")
    void checkOwner(List<Player> players, Integer testCase) {
        Player oldOwner = players.get(0);
        Player maybeNewOwner = players.get(1);
        testIsland.setOwner(oldOwner);
        oldOwner.setNumTowers(5);
        testIsland.getStudents().putAll(
        Map.of(Student.BLUE,1,  Student.RED,2,  Student.PINK,5,  Student.YELLOW,5,  Student.GREEN,4)
        );
        //oldOwner has BLUE,RED; maybeNewOwner has PINK; p3 has none.
        if (testCase==1){
            assertAll("recognize owner change",
                    ()->{
                        assertEquals(maybeNewOwner, testIsland.checkOwner(players));
                        assertAll("attribute update",
                                ()->assertEquals(testIsland.getOwner(),maybeNewOwner),
                                ()->assertEquals(maybeNewOwner.getNumTowers(),5),
                                ()->assertEquals(oldOwner.getNumTowers(),6)
                                );
                    }
            );
        }
        //oldOwner has BLUE,RED; maybeNewOwner has GREEN; p3 has none.
        else if (testCase == 2) {
            assertEquals(oldOwner,testIsland.checkOwner(players));
        }
        //oldOwner has BLUE,RED; maybeNewOwner has PINK; p3 has YELLOW.
        else if (testCase == 3) {
            assertEquals(oldOwner, testIsland.checkOwner(players));
        }
    }

    static Stream<Arguments> sourceForCheckOwner(){
        //case1 + general data
        System.setIn(new ByteArrayInputStream("player1".getBytes()));
        Player oldOwner = new Player(1,3);
        System.setIn(new ByteArrayInputStream("newOwner".getBytes()));
        Player p2 = new Player(2,3);
        oldOwner.getDiningRoom().getProfessors().putAll(Map.of(Student.BLUE,true,Student.RED,true));
        p2.getDiningRoom().getProfessors().replace(Student.PINK,true);
        List<Player> players1 = List.of(oldOwner,p2);
        //case2 data
        System.setIn(new ByteArrayInputStream("player5".getBytes()));
        Player p5 = new Player(2,3);
        p5.getDiningRoom().getProfessors().replace(Student.GREEN,true);
        List<Player> players2 = List.of(oldOwner,p5);
        //case 3 data
        System.setIn(new ByteArrayInputStream("player7".getBytes()));
        Player p7 = new Player(2,3);
        System.setIn(new ByteArrayInputStream("player8".getBytes()));
        Player p8 = new Player(2,3);
        p7.getDiningRoom().getProfessors().replace(Student.PINK,true);
        p8.getDiningRoom().getProfessors().replace(Student.YELLOW,true);
        List<Player> players3 = List.of(oldOwner,p7,p8);

        return Stream.of(arguments(players1,1),arguments(players2,2),arguments(players3,3));
    }
}