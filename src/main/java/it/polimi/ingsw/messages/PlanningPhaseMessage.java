package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.Assistant;
import javafx.application.Platform;

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


    public PlanningPhaseMessage(List<Assistant> remainingAssistants, List<Assistant> playedByOthers, String text){
        this.remainingAssistants = remainingAssistants;
        this.playedByOthers = playedByOthers;
        this.text = text;
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
        Platform.runLater(()->{
        UIManager uim = UIManager.getUIManager();
        uim.getPlanningPhaseView().fillInfo(this);
//        uim.getPlanningPhaseView().display();
        uim.getSwitcher().toAssistants();
        });
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
