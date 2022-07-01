package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveToDRCharacterTest {

    Game testGame;
    PlayerController testPc;
    MoveToDRCharacter testCharacter;
    Student testStudent;

    @BeforeEach
    void setUp(){
        testGame = Game.makeGame(2);
        testGame.doSetUp(true);
        testGame.setCurrentPlayer(testGame.getTableOrder().get(0));
        testCharacter = (MoveToDRCharacter) Character.makeCharacter(5, testGame);
        testGame.getCharacters().add(testCharacter);
        VirtualView testView = new TestVirtualView();
        testPc = new PlayerController(testGame.getCurrentPlayer(), testView);
        testStudent = testCharacter.getFirstStudent();
        testCharacter.setChosenStudent(testStudent);
    }

    @Test
    void play() throws DisconnectedException {
        testGame.getCurrentPlayer().setCoins(4);
        testCharacter.play(testGame, testPc);
        Map<Student, Integer> expected = Map.of(Student.RED, 1, Student.PINK, 0, Student.GREEN, 0, Student.BLUE, 0, Student.YELLOW, 0);
        assertEquals(expected, testGame.getCurrentPlayer().getDiningRoom().getTables());
        assertEquals(2, testGame.getCurrentPlayer().getCoins());
        assertEquals(3, testCharacter.getCost());
        //assertEquals(3, testCharacter.getStudents().size());
        testCharacter.play(testGame, testPc);
        assertEquals(2, testGame.getCurrentPlayer().getCoins());
        testGame.getCurrentPlayer().setCoins(3);
        testStudent = testCharacter.getFirstStudent();
        testCharacter.setChosenStudent(testStudent);
        testCharacter.play(testGame, testPc);
        Map<Student, Integer> expected2 = Map.of(Student.RED, 2, Student.PINK, 0, Student.GREEN, 0, Student.BLUE, 0, Student.YELLOW, 0);
        assertEquals(expected2, testGame.getCurrentPlayer().getDiningRoom().getTables());
        assertEquals(3, testCharacter.getCost());
        //assertEquals(3, testCharacter.getStudents().size());
        assertEquals(0, testGame.getCurrentPlayer().getCoins());
    }

    @Test
    void reset() {
        testCharacter.reset(testGame, testPc);
        assertEquals(null, testCharacter.getChosenStudent());
    }
}