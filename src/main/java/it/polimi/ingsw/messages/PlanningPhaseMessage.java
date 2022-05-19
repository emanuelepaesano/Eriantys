package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.Assistant;

import java.util.List;

public class PlanningPhaseMessage extends Repliable implements Message  {

    String text;

    public List<Assistant> getRemainingAssistants() {
        return remainingAssistants;
    }

    List<Assistant> remainingAssistants;

    public List<Assistant> getPlayedByOthers() {
        return playedByOthers;
    }

    List<Assistant> playedByOthers;
    Assistant chosenAssistant;


    public PlanningPhaseMessage(List<Assistant> remainingAssistants, List<Assistant> playedByOthers,String text){
        this.remainingAssistants = remainingAssistants;
        this.playedByOthers = playedByOthers;
        this.text = text;
    }

    //this would be the constructor for the client in order to reply. But a string message is actually enough
    public PlanningPhaseMessage(Assistant chosenAssistant) {
        this.chosenAssistant = chosenAssistant;
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
        return "planningview";
    }

    @Override
    public void switchAndFillView() {
        UIManager uim = UIManager.getUIManager();
        uim.getPlanningPhaseView().fillInfo(this);
        uim.getPlanningPhaseView().display(uim.getPlanningPhaseRoot());

    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public String toString() {
        return text + "\n" + remainingAssistants + "\nThe other players played: \n" + playedByOthers;
    }

    @Override
    public Boolean isRepliable() {
        return true;
    }
}
