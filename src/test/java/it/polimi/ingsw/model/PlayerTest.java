package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player testplayer;

    @BeforeEach
    void setUp(){
        System.setIn(new ByteArrayInputStream("player1".getBytes(StandardCharsets.UTF_8)));
        testplayer = new Player(1,3);
    }

    @Test
    void askWizard() {
    }

    @Test
    void askTowerColor() {
    }

    static Stream<Assistant> giveAssistants(){
        return Stream.of(Assistant.values());
    }
    @ParameterizedTest
    @MethodSource("giveAssistants")
    void playAssistant(Assistant assistant) {
        System.setIn(new ByteArrayInputStream(assistant.toString().getBytes()));
        Assistant choice = testplayer.playAssistant();
        assertEquals(choice, assistant); //this is truly only a test of the test setup...
   }

    @Test
    void doActions() {
        //for this we need also to create a map, a diningroom and a entrance
    }

    @Test
    void askMNMoves() {
    }

    @Test
    void calculateInfluence() {
    }
}