package it.polimi.ingsw.messages;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMap;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.List;

public class GenInfoMessage implements Message, Serializable {

    String text;
    private final GameMap map;
    List<Player> players;



    /**
     *     this is updated at every player's turn
     */
    public GenInfoMessage(Game game){
        this.map = game.getGameMap();
        this.players = game.getTableOrder();

        System.out.println("message: this is my map " + this.map);
        String string = "";
        string += Game.ANSI_BOLD + "GAME MAP:\n" + Game.ANSI_RESET + this.map.toString();
        string +=  Game.ANSI_BOLD + "\nPLAYERS:\n" + Game.ANSI_RESET;
        for (Player p : game.getCurrentOrder()){
            string += Game.ANSI_BOLD + p.getPlayerName() + Game.ANSI_RESET+": "+ p.getEntrance()+
                    "\n"+ p.getDiningRoom().toString()+"\n";
        }
        text = string;
    }

    @Override
    public void send(VirtualView user) { user.update(this);}

    @Override
    public void send(List<VirtualView> all) { all.forEach(v->v.update(this));}

    @Override
    public String getView() {
        return "generalview";
    }

    @Override
    public void switchAndFillView() {
        System.out.println("the message is filling a view with this map: " + map);
        Platform.runLater(()->{
            UIManager uim = UIManager.getUIManager();
            uim.getSwitcher().display();
            uim.getGenInfoView().fillInfo(this);
            System.out.println("geninfoview: finished filling info");
//        uim.getGenInfoView().display();
            uim.getSwitcher().toIslands();
        System.out.println("geninfoview displayed");
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

    @Override
    public String toString() {
        return text;
    }

    @Override
    public Boolean isRepliable() {
        return false;
    }
}
