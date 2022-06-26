package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.Player;

import java.util.List;

import static it.polimi.ingsw.messages.PlayCharMessage.PlayCharType.play;

public class PlayCharMessage extends Repliable implements Message{
    List<Character> characters;
    Player player;
    String text;

    PlayCharType type;

    public PlayCharMessage(List<Character> characters, Player player, PlayCharType type) {
        this.characters = characters;
        this.player = player;
        this.type = type;
        this.text = player.getPlayerName() +", choose a character to play, paying its cost.\n"+
                "Available coins:  " + player.getCoins() +"\n" + "Game characters:\n"+ characters +"\n" +
                "Enter a number between 1~3 to choose, or type \"back\".";
    }


    public enum PlayCharType{
        play,
        start;
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
}

