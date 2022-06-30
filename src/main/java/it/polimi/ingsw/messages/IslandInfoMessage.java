package it.polimi.ingsw.messages;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMap;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import javafx.application.Platform;

import java.util.List;


public class IslandInfoMessage extends Message {

    private int numPlayers;
    private List<List<Student>> clouds;
    private int maxMoves;
    String text;
    private GameMap map;
    List<Player> players;
    IslandInfoType type;



    /**
     *     this is updated at every player's turn
     */
    public IslandInfoMessage(Game game, IslandInfoType type){
        this.players = game.getTableOrder();
        this.numPlayers = players.size();
        this.map = game.getGameMap();
        this.clouds = game.getClouds();
        this.type = type;

        String string = "";
        string += Game.ANSI_BOLD + "GAME MAP:\n" + Game.ANSI_RESET + this.map.toString();
        string +=  Game.ANSI_BOLD + "\nPLAYERS:\n" + Game.ANSI_RESET;
        for (Player p : game.getCurrentOrder()){
            string += Game.ANSI_BOLD + p.getPlayerName() + Game.ANSI_RESET+": "+ p.getEntrance()+
                    "\n"+ p.getDiningRoom().toString()+"\n";
        }
        text = string;
    }



    public enum IslandInfoType {
        updateMap,
        init,
    }


    @Override
    public void switchAndFillView() {
        Platform.runLater(()->{
            UIManager uim = UIManager.getUIManager();
            uim.getIslandView().fillInfo(this);
        });
    }

    @Override
    public Boolean isPing() {
        return false;
    }

    public GameMap getMap() {
        return map;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<List<Student>> getClouds() {return clouds;}

    public int getNumPlayers() {
        return numPlayers;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public Boolean isRepliable() {
        return false;
    }

    public IslandInfoType getType() {
        return type;
    }

    public int getMaxMoves() {
        return maxMoves;
    }
}
