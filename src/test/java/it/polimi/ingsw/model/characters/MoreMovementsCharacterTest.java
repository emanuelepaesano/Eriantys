package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoreMovementsCharacterTest {

    Game testGame;
    PlayerController testPc;
    MoreMovementsCharacter testCharacter;

    @BeforeEach
    void setUp(){
        testGame = Game.makeGame(2);
        testGame.doSetUp(true);
        testGame.setCurrentPlayer(testGame.getTableOrder().get(0));
        testCharacter = (MoreMovementsCharacter) Character.makeCharacter(4, testGame);
        testGame.getCharacters().add(testCharacter);
        VirtualView testView = null;
        testPc = new PlayerController(testGame.getCurrentPlayer(), testView);
    }

    @Test
    void play() {
        testGame.getCurrentPlayer().setCoins(2);
        testCharacter.play(testGame, testPc);
        assertEquals(2, testGame.getCurrentPlayer().getBaseMoves());
        assertEquals(1, testGame.getCurrentPlayer().getCoins());
        assertEquals(2, testCharacter.getCost());
        testCharacter.play(testGame, testPc);
        assertEquals(1, testGame.getCurrentPlayer().getCoins());
    }

    @Test
    void reset() {
        testGame.getCurrentPlayer().setBaseMoves(2);
        testCharacter.reset(testGame, testPc);
        assertEquals(0, testGame.getCurrentPlayer().getBaseMoves());
    }
}