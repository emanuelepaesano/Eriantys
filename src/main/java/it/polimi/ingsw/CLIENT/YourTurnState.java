package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;

public class YourTurnState implements Serializable, ViewState {
    String model;


    /**
     *     this is updated at every player's turn and it can be also the model current state, when it's not your turn.
     *     You can view this whenever you want by typing "view"
     */
    public YourTurnState(Game game , Player player){
        String string = player.getPlayerName() + ", it's your turn.\n";
        if (player.getCoins() != null){
            string+= "you have" + player.getCoins() + "coins.\n";
            string+= "These are the playable characters:\n " + game.getCharacters();
        }
        string += "Your School right now: (to view others, type \"view\"\n)"+ player.getEntrance() + player.getDiningRoom();
        string += "You can choose where to move students from the entrance, " +
                "or play a character. Type \"character\", \"diningroom\" or \"islands\"";
        model = string;
    }

    @Override
    public void display() {
        System.out.println(model);
    }
}

