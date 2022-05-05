package it.polimi.ingsw.messages;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMap;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.List;

public class GenInfoMessage implements Message, Serializable {

    String content;
    GameMap map;
    List<Player> players;



    /**
     *     this is updated at every player's turn and it can be also the model current state, when it's not your turn.
     *     You can view this whenever you want by typing "view"
     */
    public GenInfoMessage(Game game){
        this.map = game.getGameMap();
        this.players = game.getTableOrder();

        String string = "";
        string += "GAME MAP:\n" + game.getGameMap().toString();
        string += "\n\nPLAYERS:\n";
        string += game.getCurrentOrder().stream().map(p->p.getPlayerName()+": "+ p.getEntrance()+ "\n"+ p.getDiningRoom().toString()+"\n").toList();
        content = string;
    }

    @Override
    public void send(VirtualView user) { user.update(this);}

    @Override
    public void send(List<VirtualView> all) { all.forEach(v->v.update(this));}

    @Override
    public String getView() {
        return "generalview";
    }

    public GameMap getMap() {
        return map;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return content;
    }
}
