package it.polimi.ingsw.CLIENT;
import it.polimi.ingsw.model.Game;

import java.io.Serializable;

public class GeneralViewState implements it.polimi.ingsw.CLIENT.ViewState, Serializable {

    String model;

    public GeneralViewState(Game game){
        String string = "";
        string += game.getGameMap().toString();
        string += game.getCurrentOrder().stream().map(p->p.getPlayerName()+": "+ p.getDiningRoom().toString()+"\n").toList();
        model = string;
    }

    @Override
    public void display() {
        System.out.println(model);
    }
}
