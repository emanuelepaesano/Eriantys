package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static it.polimi.ingsw.model.Student.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SwapEntranceDRCharacterTest {

    Game testGame;
    PlayerController testPc;
    SwapEntranceDRCharacter testCharacter;
    static private class SwapEntranceDRVirtualView extends TestVirtualView {
    int callNum = 0;
    @Override
    public String getReply() throws DisconnectedException {
        switch (callNum) {
            case 0 -> {callNum++;return "blue";}
            case 1 -> {callNum++;return "red";}
            case 2 -> {callNum++;return "pink";}
            case 3 -> {return "yellow";}
            default -> {return null;}
        }
    }
    }

    @BeforeEach
    void setUp(){
        testGame = Game.makeGame(2);
        testGame.doSetUp(true);
        testGame.setCurrentPlayer(testGame.getTableOrder().get(0));
        testCharacter = (SwapEntranceDRCharacter) Character.makeCharacter(10, testGame);
        VirtualView testView = new TestVirtualView();
        testPc = new PlayerController(testGame.getCurrentPlayer(), testView);

    }

    /**
     * @param testCase cases tested:<br>
     *                 (testCase 1) -> You don't have money to play the character, even though you have the students.
     *                 In this case, the Character is not played (returns false), you get back the coins, and the cost is not updated.<br><br>
     *         (testCase 2) -> Your Dining Room is empty. In this case you cannot play the character because you don't have students
     *                 to move to the Entrance. The same outcome as case 1 happens.<br><br>
     *         (testCase 3) -> You have both enough coins and students and the play is successful. In this case we update the Entrance,
     *                 Dining Room and Character cost.
     */
    @ParameterizedTest(name = "Testing test case {1} ...")
    @MethodSource("sourceForSwapEDR")
    void play(PlayerController pc, int testCase) throws DisconnectedException {
        switch (testCase){
            case 1->{
                //the play should fail
                assertFalse(testCharacter.play(testGame, pc));
                //entrance and dining room should not change (0 coins)
                Map<Student, Integer> expected = Map.of(RED, 3,PINK, 3, GREEN, 3, BLUE, 3, YELLOW, 3);
                assertEquals(expected,pc.getPlayer().getDiningRoom().getTables());
                assertEquals(List.of(BLUE,RED,PINK,BLUE),pc.getPlayer().getEntrance().getStudents());
            }
            case 2->{
                assertFalse(testCharacter.play(testGame, pc));
                //entrance and dining room should not change (0 coins)
                Map<Student, Integer> expected = Map.of(RED, 0,PINK, 0, GREEN, 0, BLUE, 0, YELLOW, 0);
                assertEquals(expected,pc.getPlayer().getDiningRoom().getTables(),
                        "entrance and dining room should not change.");
                assertEquals(List.of(PINK,BLUE,BLUE,RED),pc.getPlayer().getEntrance().getStudents());
                assertEquals(3,pc.getPlayer().getCoins(),"we should not charge the player");
            }

            case 3->{
                assertTrue(testCharacter.play(testGame, pc));
                Map<Student, Integer> expected = Map.of(RED, 2,PINK, 0, GREEN, 1, BLUE, 2, YELLOW, 0);
                assertEquals(expected,pc.getPlayer().getDiningRoom().getTables(),
                        "entrance and dining room should now change");
                assertEquals(List.of(PINK,BLUE,PINK,YELLOW),pc.getPlayer().getEntrance().getStudents());
                assertEquals(2,pc.getPlayer().getCoins(),"we should charge the player");
                assertEquals(2,testCharacter.getCost(),"we should update the cost");
            }

        }
    }

    static Stream<Arguments> sourceForSwapEDR(){
        //case 1: we have everything but no money. Nothing should change
        VirtualView testView1 = new SwapEntranceDRVirtualView();
        Player p1 = Player.makePlayer(1,3);
        PlayerController pc1 = new PlayerController(p1,testView1);
        p1.setCoins(0);
        p1.getEntrance().getStudents().clear();
        p1.getEntrance().getStudents().addAll(List.of(BLUE,RED,PINK,BLUE));
        p1.getDiningRoom().getTables().replace(BLUE,3);
        p1.getDiningRoom().getTables().replace(RED,3);
        p1.getDiningRoom().getTables().replace(GREEN,3);
        p1.getDiningRoom().getTables().replace(YELLOW,3);
        p1.getDiningRoom().getTables().replace(PINK,3);

        //case 2: our diningroom is empty. Nothing changes and we get money back
        VirtualView testView2 = new SwapEntranceDRVirtualView();
        Player p2= Player.makePlayer(2,3);
        PlayerController pc2 = new PlayerController(p2,testView2);
        p2.setCoins(3);
        p2.getEntrance().getStudents().clear();
        p2.getEntrance().getStudents().addAll(List.of(BLUE,RED,PINK,BLUE));
        p2.getDiningRoom().getTables().replace(BLUE,0);
        p2.getDiningRoom().getTables().replace(RED,0);
        p2.getDiningRoom().getTables().replace(GREEN,0);
        p2.getDiningRoom().getTables().replace(YELLOW,0);
        p2.getDiningRoom().getTables().replace(PINK,0);

        //case 3: everything is fine, we play the character
        VirtualView testView3 = new SwapEntranceDRVirtualView();
        Player p3 = Player.makePlayer(3,3);
        PlayerController pc3 = new PlayerController(p3,testView3);
        p3.setCoins(3);
        p3.getEntrance().getStudents().clear();
        p3.getEntrance().getStudents().addAll(List.of(BLUE,RED,PINK,BLUE));
        p3.getDiningRoom().getTables().replace(BLUE,1);
        p3.getDiningRoom().getTables().replace(RED,1);
        p3.getDiningRoom().getTables().replace(GREEN,1);
        p3.getDiningRoom().getTables().replace(YELLOW,1);
        p3.getDiningRoom().getTables().replace(PINK,1);

        return Stream.of(arguments(pc1,1),arguments(pc2,2),arguments(pc3,3));
    }



}