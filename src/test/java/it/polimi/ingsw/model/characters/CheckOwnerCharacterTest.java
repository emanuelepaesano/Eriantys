package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckOwnerCharacterTest {

    CheckOwnerCharacter testCharacter;
    Game testGame;
    PlayerController pcTest;
    Island isTest;


    @Test
    void play() throws DisconnectedException {
        testGame = Game.makeGame(2);
        testGame.doSetUp(true);
        testGame.setCurrentPlayer(testGame.getTableOrder().get(0));
        testCharacter = (CheckOwnerCharacter) Character.makeCharacter(1, testGame);
        testGame.getCharacters().add(testCharacter);
        VirtualView testView = null;
        pcTest = new PlayerController(testGame.getCurrentPlayer(), testView);
        isTest = testGame.getGameMap().getIslandById(1);
        testGame.getCurrentPlayer().getDiningRoom().getTables().putAll(Map.of(Student.BLUE,3,Student.YELLOW,5));
        testGame.getCurrentPlayer().getDiningRoom().checkAllProfessors(testGame.getTableOrder(), false);
        isTest.setStudents(Map.of(Student.BLUE, 5, Student.PINK, 0, Student.YELLOW, 0, Student.RED, 0, Student.GREEN, 0));
        testCharacter.setChosenIsland(isTest);
        testGame.getCurrentPlayer().setCoins(5);
        testCharacter.play(testGame, pcTest);
        assertEquals(testGame.getCurrentPlayer(), isTest.getOwner());
        assertEquals(2, testGame.getCurrentPlayer().getCoins());
        assertEquals(4, testCharacter.getCost());
        assertEquals(isTest, testCharacter.getChosenIsland());
        testCharacter.play(testGame, pcTest);
        assertEquals(2, testGame.getCurrentPlayer().getCoins());
        testGame.getCurrentPlayer().setCoins(4);
        testCharacter.play(testGame, pcTest);
        assertEquals(0, testGame.getCurrentPlayer().getCoins());
    }
}