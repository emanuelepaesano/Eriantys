package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckProfCharacterTest {

    Game testGame;
    PlayerController testPc;
    CheckProfCharacter testCharacter;

    @BeforeEach
    void setUp(){
        testGame = Game.makeGame(2);
        testGame.doSetUp(true);
        testGame.setCurrentPlayer(testGame.getTableOrder().get(0));
        testCharacter = (CheckProfCharacter) Character.makeCharacter(2, testGame);
        testGame.getCharacters().add(testCharacter);
        VirtualView testView = null;
        testPc = new PlayerController(testGame.getCurrentPlayer(), testView);
    }

    @Test
    void play() {
        testGame.getCurrentPlayer().setCoins(4);
        testCharacter.play(testGame, testPc);
        assertEquals(true, testGame.getCurrentPlayer().isOrEqual());
        assertEquals(2, testGame.getCurrentPlayer().getCoins());
        assertEquals(3, testCharacter.getCost());
        testCharacter.play(testGame, testPc);
        assertEquals(2, testGame.getCurrentPlayer().getCoins());
    }

    @Test
    void reset() {
        testGame.getCurrentPlayer().setOrEqual(true);
        testCharacter.reset(testGame, testPc);
        assertEquals(false, testGame.getCurrentPlayer().isOrEqual());
    }
}