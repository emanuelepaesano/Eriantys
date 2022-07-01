package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static it.polimi.ingsw.model.Student.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class BlockIslandCharacterTest {
    private static BlockIslandCharacter testCharacter;
    private static Game testGame;
    private static GameMap gameMap;
    static private class BlockIslandVirtualView extends TestVirtualView {
        @Override
        public String getReply() {
            return "10";
        }
    }


    /**
     * @param testCase cases tested:<br>
     *                 (testCase 1) -> You don't have money to play the character.
     *                 In this case, the Character is not played (returns false), you get back the coins, and the cost is not updated.<br><br>
     *         (testCase 2) -> There are no tiles left on the character. In this case the same outcome as testCase1 happens.<br><br>
     *         (testCase 3) -> There are 4 tiles on the character and you have coins. The play succeeds and one island is blocked.
     *                 We check that the block works as intended, meaning that:<br>
     *                 1. the island is blocked before Mother Nature lands there.<br>
     *                 2. when MN lands there, the owner is not updated even though it would have with influence.<br>
     *                 3. the island is unlocked after this.
     */
    @ParameterizedTest(name = "Testing test case {2} ...")
    @MethodSource("sourceForBlockIsland")
    void play(PlayerController pc, Player opponent, int testCase) throws DisconnectedException {
        switch (testCase){
            case 1->{
                //the play should fail
                assertFalse(testCharacter.play(testGame, pc),"play should fail");
                //dining room should not change (0 coins)
                gameMap.getAllIslands().forEach(island->assertFalse(island.isBlocked(),
                        "no island should have been blocked"));
                assertEquals(2,testCharacter.getCost(),"player should not be charged");
            }
            case 2->{
                assertFalse(testCharacter.play(testGame, pc), "we should annull the play");
                gameMap.getAllIslands().forEach(island->assertFalse(island.isBlocked(),
                        "no island should have been blocked"));
                assertEquals(3,pc.getPlayer().getCoins(),"player should not be charged");
                assertEquals(2,testCharacter.getCost(), "cost should not increase");
            }

            case 3->{
                testCharacter.setNumTiles(4);
                assertTrue(testCharacter.play(testGame, pc),"play should succeed");
                assertEquals(3,testCharacter.getCost(),"cost should increase.");
                assertEquals(1,pc.getPlayer().getCoins(),"player should be charged.");
                Island toTest = gameMap.getIslandById(10);
                toTest.getStudents().forEach((color,num)-> num = 0);
                toTest.getStudents().replace(BLUE,3);
                toTest.setOwner(pc.getPlayer());
                assertTrue(toTest.isBlocked(),"the island should be blocked before the checking");
                gameMap.setMotherNature(9);
                gameMap.moveMotherNatureAndCheck(List.of(pc.getPlayer(),opponent),1);
                assertEquals(pc.getPlayer(),toTest.getOwner(),"the owner should not change," +
                        "even though the opponent has the blue professor");
                assertFalse(toTest.isBlocked(),"the island now should be unlocked");
            }

        }
    }

    static Stream<Arguments> sourceForBlockIsland(){
        testGame = Game.makeGame(3);
        testGame.doSetUp(true);
        testCharacter = (BlockIslandCharacter) Character.makeCharacter(12, testGame);
        testGame.getCharacters().add(testCharacter);
        gameMap = testGame.getGameMap();
        gameMap.setBlockChar(testCharacter);

        //case 1: we have no money. Nothing should change
        VirtualView testView1 = new BlockIslandVirtualView();
        Player p1 = Player.makePlayer(1,3);
        PlayerController pc1 = new PlayerController(p1,testView1);
        p1.setCoins(0);

        //case 2: the tiles are over, same outcome
        VirtualView testView2 = new BlockIslandVirtualView();
        Player p2 = Player.makePlayer(2,3);
        PlayerController pc2 = new PlayerController(p2,testView2);
        p2.setCoins(3);
        testCharacter.setNumTiles(0);


        //case 2: we have money and we choose an island to block. the island should
        //not be verified when mn goes there and the tile should be returned to character
        VirtualView testView3 = new BlockIslandVirtualView();
        Player p3 = Player.makePlayer(3,3);
        p3.setCoins(3);
        p3.getDiningRoom().getProfessors().replaceAll((color,bool)->bool = false);
        Player p4 = Player.makePlayer(4,3);
        p4.getDiningRoom().getProfessors().replaceAll((color,bool)->bool = true);
        PlayerController pc3 = new PlayerController(p3,testView3);



        return Stream.of(arguments(pc1,p4,1),arguments(pc2,p4,2), arguments(pc3,p4,3));
    }
}