package it.polimi.ingsw.controller;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.ActionPhaseMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.*;

import java.util.*;

public class GameController {
    private Game game;

    private  List<PlayerController> controllers;
    private List<VirtualView> views;

    private List<Player> winner;

    private Boolean advanced;



    /**
     * In the constructor we can put the methods to inizialize the game.
     * Our main will call the constructor of this class to start the game
     */
    public GameController(int numplayers, List<VirtualView> views){

        if (numplayers==0){
            System.err.println("Could not start server.");
            return;
        }
        System.out.println("The views in the game controller: " + views);

        game = Game.makeGame(numplayers); //initializes the game
        this.views = views;
        //bindViewsToGame(game); the view should see the model, but it doesn't really need to
        //update by watching all of it (and sending it all...)
        bindPlayers();
        System.out.println("finito bind players");
        Boolean ad = askForAdvanced();
        this.advanced = ad;
        askAllPlayerNames();
        askAllForTC(numplayers);
        askAllForWiz();
        game.doSetUp(ad);

    }

    /**
     *
     * Binds each playerController to their player and view
     */
    private void bindPlayers(){
        controllers = new ArrayList<>();
        for (Player player: game.getTableOrder()){
            int id = player.getId();
            //We bind the first player to the first view in the list and so on
            controllers.add(new PlayerController(player, views.get(id-1)));
        }
    }


    public Boolean askForAdvanced(){
        VirtualView firstPlayer = views.get(0);
        new StringMessage("Normal game or expert version? Please type \"normal\" or \"expert\".").send(firstPlayer);
        while (true){
            String choice = Message.receive(firstPlayer).toString();
            if (choice.equalsIgnoreCase("expert")) {return true;}
            else if (choice.equalsIgnoreCase("normal")) {return false;}

            new StringMessage( Game.ANSI_RED + "What do you mean? Please type \"normal\" or \"expert\"."+ Game.ANSI_RESET)
                    .send(firstPlayer);
        }
    }




private void askAllPlayerNames(){
    List<String> usedNames = new ArrayList<>();
    for (PlayerController pc : controllers){
        String aUsedName = pc.askPlayerName(usedNames);
        usedNames.add(aUsedName);
    }
}

    /**
     * Cycles through players and asks them a color.
     * It will be stored as an attribute of the player
     */
    private void askAllForTC(int n){
        ArrayList<TowerColor> remainingColors;
        if (n==3) {
            remainingColors = new ArrayList<>(Arrays.asList(TowerColor.values()));
        }
        else{
            remainingColors = new ArrayList<>(Arrays.asList(TowerColor.WHITE,TowerColor.BLACK));
        }
        for (PlayerController pc: controllers){
            TowerColor c = pc.askTowerColor(remainingColors);
            remainingColors.remove(c);
        }
    }

    /**
     * Cycles through players and asks them a wizard number.
     * It will be stored as an attribute of the player
     */
    public void askAllForWiz() {
        ArrayList<Integer> remainingWizards = new ArrayList<>(Arrays.asList(1,2,3,4));
        for (PlayerController pc : controllers){
            Integer wiz = pc.askWizard(remainingWizards);
            remainingWizards.remove(wiz);
        }
    }


    /**
     * I put this here for now but maybe we can do a planningPhaseController.
     * This is a bit complicated, we might break it down somehow.
     */
    public void doPlanningPhase(Game g){
        // TODO: 13/04/2022 we miss the case in which the player only has the same assistant left
        //This weird code is to start from the current first and then go clockwise, following the
        //table order. In this order, we make players play assistants, and store them in a Map
        Map<Integer, Player> Priorities = new TreeMap<>();
        List<Assistant> playedAssistants = new ArrayList<>();
        int initialind = g.getTableOrder().indexOf(g.getCurrentOrder().get(0)); //this is the index in the tableOrder of current first
        for (int i = initialind; i<initialind+g.numPlayers;i++) {
            PlayerController p = controllers.get(i%g.numPlayers);
            Assistant choice = p.playAssistant(playedAssistants);
            playedAssistants.add(choice);
            Priorities.put(choice.getPriority(),p.getPlayer());
        }
        //The second part uses the Map to make a new currentOrder
        List<Player> newOrder = new ArrayList<>();
        for (int i = 0; i< g.numPlayers;i++){
            Player first = Priorities.remove(Collections.min(Priorities.keySet()));
            newOrder.add(first);
        }
        new StringMessage("Player order for this turn:" + newOrder).send(views);
        g.setCurrentOrder(newOrder);
    }
    /**
     * This is the main method for the action phase of each player. It asks the player which action they want to do
     * and then performs the action, until they used all of their moves.
     */
    public void doActions(PlayerController pc){
        Player player = pc.getPlayer();
        EntranceController entranceController = pc.getEntranceController();
        int availableActions = player.getNumActions();
        //there will be an actionlistener in the client linked to 3 buttons. depending on the button pressed
        //(movetodiningroom or movetoisland) the client sends a different string.
        // the controller calls a method based on this string, updating model
        while (availableActions>0) {
            String action = askWhichAction(availableActions,pc);
            if (action.equalsIgnoreCase("diningroom")) {
                availableActions -= entranceController.moveToDiningRoom(availableActions, player.getDiningRoom());
                player.getDiningRoom().checkProfessors(game.getTableOrder(),player.isOrEqual());
            }
            else if (action.equalsIgnoreCase("islands")){
                availableActions -= entranceController.moveToIsland(availableActions, game.getGameMap());}
            else if (action.equalsIgnoreCase("characters")){
                if(player.getCoins()!= null){
                    pc.playCharacters(game.getCharacters(),game);
                }
                else {
                    new StringMessage("This is not an advanced game!").send(pc.getPlayerView());
                }
            }
        }
        new StringMessage("After your moves: " + player.getDiningRoom()).send(pc.getPlayerView());
    }
    public String askWhichAction(int availableActions, PlayerController pc){
        new ActionPhaseMessage(advanced, availableActions,pc.getPlayer(), game.getCharacters()).send(pc.getPlayerView());
        return Message.receive(pc.getPlayerView()).toString();
    }


    public void setCurrentPlayer(Player player){
        //the controller of that player...
        PlayerController currentPC = controllers.get(controllers.stream().map(PlayerController::getPlayer).toList().indexOf(player));
        game.setCurrentPlayer(currentPC.getPlayer());
    }



    //GETTER
    public Game getGame() {
        return game;
    }

    public List<PlayerController> getControllers() {
        return controllers;
    }
}
