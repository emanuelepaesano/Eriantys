package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static it.polimi.ingsw.model.Student.*;
import static it.polimi.ingsw.model.Student.RED;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ReplaceFromEntranceCharacterTest {

    private static ReplaceFromEntranceCharacter testCharacter;

    private static Game testGame;

    static private class TestCase2VirtualView extends TestVirtualView {
        @Override
        public String getReply() throws DisconnectedException {
            return "back";
        }
    }
    static private class TestCase3VirtualView extends TestVirtualView {
        int callNum = 0;
        @Override
        public String getReply() throws DisconnectedException {
            switch (callNum) {
                case 0 -> {callNum++;return "pink";}
                case 1 -> {callNum++;return "blue";}
                case 2 -> {callNum++;return "yellow";}
                case 3 -> {callNum++;return "red";}
                case 4 -> {callNum++;return "red";}
                case 5 -> {callNum++;return "red";}
                default -> {return null;}
            }
        }
    }


    /**
     * @param testCase cases tested:<br>
     *                 (testCase 1) -> You don't have money to play the character.
     *                 In this case, the Character is not played (returns false), you get back the coins, and the cost is not updated.<br><br>
     *         (testCase 2) -> You press on "back" right away and the character play is annulled. The same outcome
     *                 ase case 1 should happen.<br><br>
     *         (testCase 3) -> You have enough coins and the play succeeds. You exchange 3 students from the character and 3
     *                 from your Entrance.
     */
    @ParameterizedTest(name = "Testing test case {1} ...")
    @MethodSource("sourceForReplaceEntrance")
    void play(PlayerController pc, int testCase) throws DisconnectedException {
        switch (testCase){
            case 1->{
                //the play should fail
                assertFalse(testCharacter.play(testGame, pc),"play should fail");
                //dining room should not change (0 coins)
                List<Student> expected = List.of(RED,RED,RED,BLUE,GREEN,YELLOW,PINK);
                assertEquals(expected,pc.getPlayer().getEntrance().getStudents());
                assertEquals(2,testCharacter.getCost(),"cost should not increase");
            }
            case 2->{
                assertFalse(testCharacter.play(testGame, pc), "we should annull the play");
                List<Student> expected = List.of(RED,RED,RED,BLUE,GREEN,YELLOW,PINK);
                assertEquals(expected,pc.getPlayer().getEntrance().getStudents());
                assertEquals(2,testCharacter.getCost(), "cost should not increase");
                assertEquals(3,pc.getPlayer().getCoins(),"player should not be charged");
            }

            case 3->{
                assertTrue(testCharacter.play(testGame, pc),"play should succeed");
                //dining room of players should change like this
                List<Student> expectedCharacter = List.of(GREEN,BLUE,BLUE,RED,RED,RED);
                List<Student> expectedEntrance = List.of(BLUE,GREEN,YELLOW,PINK,PINK,BLUE,YELLOW);
                assertEquals(expectedCharacter,testCharacter.getStudents());
                assertEquals(expectedEntrance,pc.getPlayer().getEntrance().getStudents());
                assertEquals(3,testCharacter.getCost(),"cost should increase.");
                assertEquals(2,pc.getPlayer().getCoins(),"player should be charged.");
            }

        }
    }

    static Stream<Arguments> sourceForReplaceEntrance(){
        testGame = Game.makeGame(3);
        testGame.doSetUp(true);
        testCharacter = (ReplaceFromEntranceCharacter) Character.makeCharacter(9, testGame);
        testGame.getCharacters().add(testCharacter);
        //case 1: we have no money. Nothing should change
        VirtualView testView1 = new TestCase2VirtualView();
        testCharacter.getStudents().clear();
        testCharacter.getStudents().addAll(List.of(PINK,BLUE,YELLOW,GREEN,BLUE,BLUE));
        Player p1 = Player.makePlayer(1,3);
        PlayerController pc1 = new PlayerController(p1,testView1);
        p1.setCoins(0);
        p1.getEntrance().getStudents().clear();
        p1.getEntrance().getStudents().addAll(List.of(RED,RED,RED,BLUE,GREEN,YELLOW,PINK));


        //case 2: we say back right away and the play is annulled
        VirtualView testView2 = new TestCase2VirtualView();
        testCharacter.getStudents().clear();
        testCharacter.getStudents().addAll(List.of(PINK,BLUE,YELLOW,GREEN,BLUE,BLUE));
        Player p2 = Player.makePlayer(2,3);
        PlayerController pc2 = new PlayerController(p2,testView2);
        p2.setCoins(3);
        p2.getEntrance().getStudents().clear();
        p2.getEntrance().getStudents().addAll(List.of(RED,RED,RED,BLUE,GREEN,YELLOW,PINK));

        //case 3: we play the character and choose 3 from entrance and 3 from character
        VirtualView testView3 = new TestCase3VirtualView();
        testCharacter.getStudents().clear();
        testCharacter.getStudents().addAll(List.of(PINK,BLUE,YELLOW,GREEN,BLUE,BLUE));
        Player p3 = Player.makePlayer(3,3);
        PlayerController pc3 = new PlayerController(p3,testView3);
        p3.getEntrance().getStudents().clear();
        p3.getEntrance().getStudents().addAll(List.of(RED,RED,RED,BLUE,GREEN,YELLOW,PINK));
        p3.setCoins(4);

        return Stream.of(arguments(pc1,1),arguments(pc2,2),arguments(pc3,3));
}
}