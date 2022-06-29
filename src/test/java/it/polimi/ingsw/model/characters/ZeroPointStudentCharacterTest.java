package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ZeroPointStudentCharacterTest {

    Game testGame;
    PlayerController testPc;
    ZeroPointStudentCharacter testCharacter;
    Student testStudent;

    @BeforeEach
    void setUp(){
        testGame = Game.makeGame(2);
        testGame.doSetUp(true);
        testGame.setCurrentPlayer(testGame.getTableOrder().get(0));
        testCharacter = (ZeroPointStudentCharacter) Character.makeCharacter(8, testGame);
        testGame.getCharacters().add(testCharacter);
        VirtualView testView = null;
        testPc = new PlayerController(testGame.getCurrentPlayer(), testView);
        testStudent=Student.RED;
        testCharacter.setChosenStudent(testStudent);
    }

    @Test
    void play() {
        testGame.getCurrentPlayer().setCoins(6);
        for (int i = 0; i<testGame.getGameMap().getArchipelago().size(); i++){
            testGame.getGameMap().getIslandById(i).setStudents(new HashMap<>(Map.of(Student.RED, 2, Student.PINK,0,Student.GREEN,0,Student.BLUE,0,Student.YELLOW,0)));
        }
        testCharacter.play(testGame, testPc);
        for (int i=0; i<testGame.getGameMap().getArchipelago().size(); i++){
            assertEquals(0, testGame.getGameMap().getIslandById(i).getStudents().get(Student.RED));
        }
        assertEquals(3, testGame.getCurrentPlayer().getCoins());
        assertEquals(4, testCharacter.getCost());
        testCharacter.play(testGame, testPc);
        assertEquals(3, testGame.getCurrentPlayer().getCoins());
    }

    @Test
    void reset() {
        testGame.getCurrentPlayer().setCoins(3);
        for (int i = 0; i<testGame.getGameMap().getArchipelago().size(); i++){
            testGame.getGameMap().getIslandById(i).setStudents(new HashMap<>(Map.of(Student.RED, 2, Student.PINK,0,Student.GREEN,0,Student.BLUE,0,Student.YELLOW,0)));
        }
        testCharacter.play(testGame, testPc);
        testCharacter.reset(testGame, testPc);
        for (int j = 0; j<testGame.getGameMap().getArchipelago().size(); j++){
            assertEquals(2, testGame.getGameMap().getIslandById(j).getStudents().get(Student.RED));
        }
    }
}