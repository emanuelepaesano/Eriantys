package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.Entrance;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class PlayerController {
    private int id;
    private Player player;
    private int numPlayers;


    public PlayerController(int id, Game game){ //if this gives weird errors, go back to int numplayers instead of game
        //the id of the playercontroller and its player coincide, might turn out useful
        this.id = id;
        numPlayers = game.numPlayers;
        player = new Player(id,game);

        //it's important to make the dining room before the entrance
    }

    // TODO: 14/04/2022 this is now here, but we can do an ActionPhaseController for every player + turn.
    //  probably here is fine
    public void doActions(){
        int availableActions = (numPlayers == 3 ? 4:3 );
        //this will be an actionlistener linked to 2 buttons. depending on the button pressed
        //(movetodiningroom or movetoisland) the controller calls a different method, then updates model
        //int usedActions = 0
        //while usedActions < availableActions:
        //  askWhichAction(); //this will come from the view, so it must be in a controller
        //  if actionperformed == 'diningroom':
        //      usedmoves += p.entrance.movetoDiningRoom(availableMoves)
        //  else:
        //      usedmoves += p.entrance.movetoIsland(availableMoves)

        //These methods need to know the availableMoves so that player can move more than
        //1 student at once and then we can subtract all the moves from the available.
        //They can return the number of moves used, so we can do this.
        // They also need to give an option to go back if player changes his mind (choosing 0 is enough)
    }


    public Player getPlayer() {
        return player;
    }
}
