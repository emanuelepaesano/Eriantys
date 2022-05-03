package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class PlanningPhaseState implements ViewState{
    String model;

    public PlanningPhaseState(Game game, Player player) {
        String string = "";
        string += "Your available assistants are" + player.getAssistants();
        model = string;

    }

    @Override
    public void display() {
        System.out.println(model);
    }
}
