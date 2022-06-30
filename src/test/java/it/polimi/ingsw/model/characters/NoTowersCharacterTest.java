package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoTowersCharacterTest {

    Game testGame;
    PlayerController testPc;
    NoTowersCharacter testCharacter;

    @BeforeEach
    void setUp(){
        testGame = Game.makeGame(2);
        testGame.doSetUp(true);
        testGame.setCurrentPlayer(testGame.getTableOrder().get(0));
        testCharacter = (NoTowersCharacter) Character.makeCharacter(6, testGame);
        testGame.getCharacters().add(testCharacter);
        VirtualView testView = null;
        testPc = new PlayerController(testGame.getCurrentPlayer(), testView);
        testGame.getGameMap().getIslandById(0).setSize(3);
    }

    @Test
    void play() {
        testGame.getCurrentPlayer().setCoins(6);
        testCharacter.play(testGame, testPc);
        assertEquals(0, testGame.getGameMap().getIslandById(0).getSize());
        assertEquals(3, testCharacter.getOldsizes().get(0));
        assertEquals(3, testGame.getCurrentPlayer().getCoins());
        assertEquals(4, testCharacter.getCost());
        testCharacter.play(testGame, testPc);
        assertEquals(3, testGame.getCurrentPlayer().getCoins());
    }

    @Test
    void reset() {
        testGame.getGameMap().getIslandById(1).setSize(5);
        testGame.getCurrentPlayer().setCoins(6);
        testCharacter.play(testGame, testPc);
        testCharacter.reset(testGame, testPc);
        assertEquals(5, testGame.getGameMap().getIslandById(1).getSize());
        assertEquals(null, testCharacter.getOldsizes());
    }
}