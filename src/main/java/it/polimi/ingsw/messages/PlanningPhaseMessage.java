package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.model.Assistant;

import java.util.List;

public class PlanningPhaseMessage extends Repliable {

    String text;

    List<Assistant> remainingAssistants;

    List<Assistant> playedByOthers;

    Assistant current;

    PlanningPhaseType type;

    public PlanningPhaseMessage(List<Assistant> remainingAssistants, List<Assistant> playedByOthers, String text){
        this.remainingAssistants = remainingAssistants;
        this.playedByOthers = playedByOthers;
        this.text = text;
        this.type = PlanningPhaseType.ACTION;
    }

    public PlanningPhaseMessage(Assistant current){
        this.current = current;
        this.type = PlanningPhaseType.UPDATE;
    }

    public enum PlanningPhaseType{
        ACTION,
        UPDATE;

    }
    @Override
    public void switchAndFillView() {
        UIManager uim = UIManager.getUIManager();
        uim.getPlanningPhaseView().fillInfo(this);
        uim.getSwitcher().toAssistants();
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

    public List<Assistant> getRemainingAssistants() {
        return remainingAssistants;
    }

    public List<Assistant> getPlayedByOthers() {
        return playedByOthers;
    }

    public PlanningPhaseType getType() {
        return type;
    }

    public Assistant getCurrent() {
        return current;
    }
}
