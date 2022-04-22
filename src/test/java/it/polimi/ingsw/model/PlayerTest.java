package it.polimi.ingsw.model;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void askWizard() {
        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream("playername".getBytes());
        System.setIn(in);

        Player player = new Player(2,3);
        System.setIn(sysInBackup);
    }

    @Test
    void askTowerColor() {
    }

    @Test
    void playAssistant() {
    }

    @Test
    void doActions() {
    }

    @Test
    void askMNMoves() {
    }

    @Test
    void calculateInfluence() {
    }

    @Test
    void checkNumTowers() {
    }

    @Test
    void testToString() {
    }

    @Test
    void setNumTowers() {
    }

    @Test
    void getAssistants() {
    }

    @Test
    void getPlayerName() {
    }

    @Test
    void getDiningRoom() {
    }

    @Test
    void getEntrance() {
    }

    @Test
    void getNumTowers() {
    }
}