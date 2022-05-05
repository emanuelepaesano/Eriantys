package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.characters.Character;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class PlayCharMessage implements Message{
    List<Character> characters;
    Player player;
    String text;

    public PlayCharMessage(List<Character> characters, Player player) {
        this.characters = characters;
        this.player = player;
        this.text = player.getPlayerName() +", choose a character to play, paying its cost.\n"+
                "Available coins:  " + player.getCoins() +"\n" + "Game characters:\n"+ characters +"\n " +
                "Enter a number between 1~3 to choose.";
    }

    @Override
    public void send(VirtualView user) {
        user.update(this);
    }

    @Override
    public void send(List<VirtualView> all) {
        all.forEach(v->v.update(this));
    }

    @Override
    public String getView() {
        return "playcharview";
    }

    @Override
    public String toString() {
        return this.text;
    }
}
