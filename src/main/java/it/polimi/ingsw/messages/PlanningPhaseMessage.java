package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.Assistant;

import java.util.List;

public class PlanningPhaseMessage implements Message {

    List<Assistant> remainingAssistants;
    List<Assistant> playedByOthers;
    Assistant chosenAssistant;

    @Override
    public void send(VirtualView user) {

    }

    @Override
    public void send(List<VirtualView> all) {

    }

    @Override
    public String getView() {
        return "planningview";
    }
}
