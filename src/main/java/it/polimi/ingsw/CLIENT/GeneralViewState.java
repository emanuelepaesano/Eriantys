package it.polimi.ingsw.CLIENT;
import it.polimi.ingsw.model.Game;

import java.io.Serializable;

public class GeneralViewState implements it.polimi.ingsw.CLIENT.ViewState, Serializable {

    String model;


    /**
     *     this is updated at every player's turn and it can be also the model current state, when it's not your turn.
     *     You can view this whenever you want by typing "view"
     */
    public GeneralViewState(Game game){
        String string = "";
        string += "GAME MAP:\n game.getGameMap().toString()";
        string += "\n\nPLAYERS:\n";
        string += game.getCurrentOrder().stream().map(p->p.getPlayerName()+": "+ p.getEntrance()+ "\n"+ p.getDiningRoom().toString()+"\n").toList();
        model = string;
    }

    @Override
    public void display() {
        System.out.println(model);
    }
}
