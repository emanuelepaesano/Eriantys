package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class PlayCharMessage extends Repliable{
    List<Character> characters;
    Player player;
    String text;

    public PlayCharMessage(List<Character> characters, Player player) {
        this.characters = characters;
        this.player = player;
        this.text = player.getPlayerName() +", choose a character to play, paying its cost.\n"+
                "Available coins:  " + player.getCoins() +"\n" + "Game characters:\n"+ characters +"\n" +
                "Enter a number between 1~3 to choose, or type \"back\".";
    }


    @Override
    public void switchAndFillView() {

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
}

