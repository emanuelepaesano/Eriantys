package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlaceInIslandCharacterTest {

    Game testGame;
    PlayerController testPc;
    PlaceInIslandCharacter testCharacter;
    Student testStudent;
    Island testIsland;

    @BeforeEach
    void setUp(){
        testGame = Game.makeGame(2);
        testGame.doSetUp(true);
        testGame.setCurrentPlayer(testGame.getTableOrder().get(0));
        testCharacter = (PlaceInIslandCharacter) Characters.makeCharacter(7, testGame);
        testGame.getCharacters().add(testCharacter);
        VirtualView testView = null;
        testPc = new PlayerController(testGame.getCurrentPlayer(), testView);
        testStudent = testCharacter.getFirstStudent();
        testCharacter.setChosenStudent(testStudent);
        testGame.getGameMap().getIslandById(0).setStudents(new HashMap<>(Map.of(Student.RED, 0, Student.PINK, 0, Student.GREEN, 0, Student.BLUE, 0, Student.YELLOW, 0)));
        testIsland = testGame.getGameMap().getIslandById(0);
        testCharacter.setChosenIsland(testIsland);

    }

    @Test
    void play() {
        testGame.getCurrentPlayer().setCoins(2);
        testCharacter.play(testGame, testPc);
        Map<Student, Integer> expected = Map.of(Student.RED, 1, Student.PINK, 0, Student.GREEN, 0, Student.BLUE, 0, Student.YELLOW, 0);
        assertEquals(expected, testGame.getGameMap().getIslandById(0).getStudents());
        assertEquals(1, testGame.getCurrentPlayer().getCoins());
        assertEquals(2, testCharacter.getCost());
        testCharacter.play(testGame, testPc);
        assertEquals(1, testGame.getCurrentPlayer().getCoins());
    }

    @Test
    void reset() {
        testGame.getCurrentPlayer().setCoins(2);
        testCharacter.play(testGame, testPc);
        testCharacter.reset(testGame, testPc);
        assertEquals(null, testCharacter.getChosenIsland());
        assertEquals(null, testCharacter.getChosenStudent());
    }
}