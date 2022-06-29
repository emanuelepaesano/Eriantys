package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Character;

import java.util.List;

import static it.polimi.ingsw.messages.PlayCharMessage.PlayCharType.chooseStudent;
import static it.polimi.ingsw.messages.PlayCharMessage.PlayCharType.play;

public class PlayCharMessage extends Repliable implements Message{
    List<Character> characters;
    Player player;
    String text;
    int charIndex = -1;
    PlayCharType type;
    List<Student> tempStudents;
    public PlayCharMessage(List<Character> characters, Player player) {
        this.characters = characters;
        this.player = player;
        this.type = type;
        this.text = player.getPlayerName() +", choose a character to play, paying its cost.\n"+
                "Available coins:  " + player.getCoins() +"\n" + "Game characters:\n"+ characters +"\n" +
                "Enter a number between 1~3 to choose, or type \"back\".";
    }

    public PlayCharMessage(List<Student> students, int indexActive) {

        this.tempStudents = students;
        this.charIndex = indexActive;
        this.type = chooseStudent;
        this.text = "You can pick a student from the character:\n" +
                students;
    }


    public enum PlayCharType{
        play,
        start,
        chooseStudent;
    }


    @Override
    public void switchAndFillView() {
        UIManager uim = UIManager.getUIManager();
        uim.getCharactersView().fillInfo(this);
        if (type.equals(play)){
            uim.getSwitcher().toCharacters();
        }
    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public String toString() {
        return this.text;
    }

    @Override
    public Boolean isRepliable() {
        return true;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayCharType getType() {
        return type;
    }

    public int getCharIndex() {
        return charIndex;
    }

    public List<Student> getTempStudents() {
        return tempStudents;
    }
}

