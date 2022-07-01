package it.polimi.ingsw.messages;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class EndGameMessage extends Message {


    List<Player> otherWinners;
    EndGameType type;

    String text;

    public EndGameMessage(List<Player> otherWinners, EndGameType type){
        this.type = type;
        this.otherWinners = otherWinners;
        switch (type){
            case TIE -> text = "Game is over! \n\n Game is a tie! You tie with " + otherWinners.toString() + " .";
            case WIN -> text = "Game is over! \n\n You won, Congratulations!!! ";
            case LOSE -> text = "Game is over! \n\n You lost... Better luck next time :) \n" + otherWinners + "won the game.";
        }


    }


    @Override
    public void switchAndFillView() {
        //mettiamo la endgameview e chiudiamo lo switcher
        UIManager uim = UIManager.getUIManager();
        uim.getEndGameView().fillInfo(this);
        uim.getEndGameView().display();

    }

    public enum EndGameType{
        TIE,
        WIN,
        LOSE;
    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public Boolean isRepliable() {
        return false;
    }

    public EndGameType getType() {
        return type;
    }

    public List<Player> getOtherWinners() {
        return otherWinners;
    }

    @Override
    public String toString() {
        return text;
    }
}
