package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;

import java.util.List;

import static it.polimi.ingsw.messages.IslandActionMessage.IslandActionType.moveMN;

public class IslandActionMessage extends Repliable {

    int maxMoves;
    IslandActionType type;
    String text;


    public IslandActionMessage(Player player, int maxMoves){
        this.maxMoves = maxMoves;
        this.type = moveMN;
        text = player.getPlayerName() + ", how many steps do you want to move Mother Nature? " +
                "(At least 1, maximum " + maxMoves + ")";
    }

    @Override
    public void send(VirtualView user) {
        user.update(this);
    }

    @Override
    public void send(List<VirtualView> all) {
        all.forEach((v)->v.update(this));
    }

    @Override
    public String getView() {
        return null;
    }

    @Override
    public void switchAndFillView() {
        Platform.runLater(()->{
            UIManager uim = UIManager.getUIManager();
            uim.getSwitcher().toIslands();
            uim.getGenInfoView().fillInfo(this);
        });
    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public Boolean isRepliable() {
        return true;
    }

    public enum IslandActionType{
        moveMN,
        cloudSel;
    }

    public int getMaxMoves() {
        return maxMoves;
    }

    public IslandActionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return text;
    }
}
