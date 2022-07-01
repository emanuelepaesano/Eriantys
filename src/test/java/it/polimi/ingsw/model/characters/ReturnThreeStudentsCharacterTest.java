package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static it.polimi.ingsw.model.Student.*;
import static it.polimi.ingsw.model.Student.PINK;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ReturnThreeStudentsCharacterTest {

    private static ReturnThreeStudentsCharacter testCharacter;

    static private class ReturnThreeVirtualView extends TestVirtualView {
        @Override
        public String getReply() throws DisconnectedException {
            return "red";
            }
    }

    @BeforeEach
    void setUp(){
        Game testGame = Game.makeGame(3);
        testGame.doSetUp(true);
        testCharacter = (ReturnThreeStudentsCharacter) Character.makeCharacter(11, testGame);
    }

    /**
     * @param testCase cases tested:<br>
     *                 (testCase 1) -> You don't have money to play the character.
     *                 In this case, the Character is not played (returns false), you get back the coins, and the cost is not updated.<br><br>
     *         (testCase 2) -> You have enough coins and the play succeeds with everyone having 3 or more students of
     *                 the selected color (always red in the tests)<br><br>
     *         (testCase 3) -> You have enough coins and the play succeeds with someone having less than 3 students of the selected
     *                 color.
     */
    @ParameterizedTest(name = "Testing test case {2} ...")
    @MethodSource("sourceForReturnThree")
    void play(PlayerController pc, Game game, int testCase) throws DisconnectedException {
        switch (testCase){
            case 1->{
                //the play should fail
                assertFalse(testCharacter.play(game, pc),"play should fail");
                //dining room should not change (0 coins)
                Map<Student, Integer> expected = Map.of(RED, 3,PINK, 0, GREEN, 0, BLUE, 0, YELLOW, 0);
                assertEquals(expected,game.getTableOrder().get(0).getDiningRoom().getTables());
                assertEquals(expected,game.getTableOrder().get(1).getDiningRoom().getTables());
                assertEquals(expected,game.getTableOrder().get(2).getDiningRoom().getTables());
                assertEquals(3,testCharacter.getCost(),"cost should not increase");
            }
            case 2->{
                //the play should now succeed
                assertTrue(testCharacter.play(game, pc));
                //dining room of players should change like this
                Map<Student, Integer> expected1 = Map.of(RED, 0,PINK, 0, GREEN, 0, BLUE, 0, YELLOW, 0);
                Map<Student, Integer> expected2 = Map.of(RED, 1,PINK, 0, GREEN, 0, BLUE, 0, YELLOW, 0);
                Map<Student, Integer> expected3 = Map.of(RED, 2,PINK, 0, GREEN, 0, BLUE, 0, YELLOW, 0);
                assertEquals(expected1,game.getTableOrder().get(0).getDiningRoom().getTables());
                assertEquals(expected2,game.getTableOrder().get(1).getDiningRoom().getTables());
                assertEquals(expected3,game.getTableOrder().get(2).getDiningRoom().getTables());
                assertEquals(4,testCharacter.getCost(),"cost should increase.");
                assertEquals(0,pc.getPlayer().getCoins(),"player should be charged.");
            }

            case 3->{
                assertTrue(testCharacter.play(game, pc),"play should succeed");
                //dining room of players should change like this
                Map<Student, Integer> expected1 = Map.of(RED, 0,PINK, 0, GREEN, 0, BLUE, 0, YELLOW, 0);
                Map<Student, Integer> expected2 = Map.of(RED, 2,PINK, 0, GREEN, 0, BLUE, 0, YELLOW, 0);
                Map<Student, Integer> expected3 = Map.of(RED, 0,PINK, 0, GREEN, 0, BLUE, 0, YELLOW, 0);
                assertEquals(expected1,game.getTableOrder().get(0).getDiningRoom().getTables());
                assertEquals(expected2,game.getTableOrder().get(1).getDiningRoom().getTables());
                assertEquals(expected3,game.getTableOrder().get(2).getDiningRoom().getTables());
                assertEquals(4,testCharacter.getCost(),"cost should not increase further.");
                assertEquals(1,pc.getPlayer().getCoins(),"player should be charged.");
            }

        }
    }

    static Stream<Arguments> sourceForReturnThree(){
        VirtualView testView = new ReturnThreeVirtualView();
        //case 1: we have no money. Nothing should change
        Game game1 = Game.makeGame(3);
        game1.doSetUp(true);
        game1.getCharacters().add(testCharacter);
        Player p11 = game1.getTableOrder().get(0);
        PlayerController pc1 = new PlayerController(p11,testView);
        Player p12 = game1.getTableOrder().get(1);
        Player p13 = game1.getTableOrder().get(2);
        p11.setCoins(0);
        p11.getDiningRoom().getTables().replace(RED,3);
        p12.getDiningRoom().getTables().replace(RED,3);
        p13.getDiningRoom().getTables().replace(RED,3);


        //case 2: we have money and everyone has 3 or more red students
        Game game2 = Game.makeGame(3);
        game2.doSetUp(true);
        game2.getCharacters().add(testCharacter);
        Player p21 = game2.getTableOrder().get(0);
        PlayerController pc2 = new PlayerController(p21,testView);
        Player p22 = game2.getTableOrder().get(1);
        Player p23 = game2.getTableOrder().get(2);
        p21.setCoins(3);
        p21.getDiningRoom().getTables().replace(RED,3);
        p22.getDiningRoom().getTables().replace(RED,4);
        p23.getDiningRoom().getTables().replace(RED,5);

        //case 3: we have money and someone has less than 3 red students
        Game game3 = Game.makeGame(3);
        game3.doSetUp(true);
        game3.getCharacters().add(testCharacter);
        Player p31 = game3.getTableOrder().get(0);
        PlayerController pc3 = new PlayerController(p31,testView);
        Player p32 = game3.getTableOrder().get(1);
        Player p33 = game3.getTableOrder().get(2);
        p31.setCoins(4);
        p31.getDiningRoom().getTables().replace(RED,1);
        p32.getDiningRoom().getTables().replace(RED,5);
        p33.getDiningRoom().getTables().replace(RED,0);

        return Stream.of(arguments(pc1,game1,1),arguments(pc2,game2,2),arguments(pc3,game3,3));
    }

}