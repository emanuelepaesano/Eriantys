package it.polimi.ingsw.controller;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;

import java.util.*;

import static it.polimi.ingsw.messages.ActionPhaseMessage.ActionPhaseType.update;

public class GameController {
    private Game game;

    private  List<PlayerController> controllers;
    private List<VirtualView> views;

    private List<Player> winner;

    VirtualView firstPlayer;
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
        firstPlayer = views.get(0);
        bindPlayers();
        while (advanced == null) {
            askForAdvanced();
            replyToAdvanced(firstPlayer.getReply());
        }
        askAllPlayerNames();
        askAllForTC(numplayers);
        askAllForWiz();
        game.doSetUp(this.advanced);

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


    public void askForAdvanced(){
        new FirstClientMessage("Normal game or expert version? Please type \"normal\" or \"expert\".").send(firstPlayer);
    }

    public void replyToAdvanced(String choice){
            if (choice.equalsIgnoreCase("expert")) {this.advanced = true;return;}
            else if (choice.equalsIgnoreCase("normal")) {this.advanced = false;return;}
            new NoReplyMessage( Game.ANSI_RED + "What do you mean? Please type \"normal\" or \"expert\"."+ Game.ANSI_RESET)
                    .send(firstPlayer);
    }




private void askAllPlayerNames(){
    List<String> usedNames = new ArrayList<>();
    for (PlayerController pc : controllers){
        String aUsedName= null;
        while(aUsedName == null) {
            pc.askPlayerName();
            String reply = pc.getPlayerView().getReply();
            aUsedName = pc.replyToPlayerName(reply, usedNames);
        }
        usedNames.add(aUsedName);
    }
}

    /**
     * Cycles through players and asks them a color.
     * It will be stored as an attribute of the player
     */
    private void askAllForTC(int n){
        ArrayList<TowerColor> remainingColors;
        if (n==3) {remainingColors = new ArrayList<>(Arrays.asList(TowerColor.values()));}
        else{remainingColors = new ArrayList<>(Arrays.asList(TowerColor.WHITE,TowerColor.BLACK));}
        for (PlayerController pc: controllers){
            TowerColor c = null;
            while (c == null) {
                pc.askTowerColor(remainingColors);
                String reply = pc.getPlayerView().getReply();
                c = pc.replyToTowerColor(reply, remainingColors);
            }
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
            Integer wiz = null;
            while(wiz == null) {
                pc.askWizard(remainingWizards);
                try{
                    Integer input = Integer.parseInt(pc.getPlayerView().getReply());
                    wiz = pc.replyToWizard(input, remainingWizards);
                    remainingWizards.remove(wiz);
                }catch (Exception ignored){}
            }
        }
    }


    /**
     * I put this here for now but maybe we can do a planningPhaseController.
     * This is a bit complicated, we might break it down somehow.
     */
    public void doPlanningPhase(Game g){
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
        while (availableActions>0) {
            String action = askWhichAction(availableActions,pc);
            if (action.equalsIgnoreCase("diningroom")) {
                availableActions -= entranceController.moveToDiningRoom(availableActions, player.getDiningRoom(), game.getTableOrder());
                new ActionPhaseMessage(player,update).send(pc.getPlayerView());
            }
            else if (action.equalsIgnoreCase("islands")){
                int didMove = entranceController.moveToIsland(game.getGameMap());
                if (didMove == 1){
                    new GenInfoMessage(game).send(pc.getPlayerView());
                    availableActions -= didMove;
                }
            }
            else if (action.equalsIgnoreCase("characters")){
                if(player.getCoins()!= null){
                    pc.playCharacters(game.getCharacters(),game);
                }
                else {
                    new NoReplyMessage("This is not an advanced game!").send(pc.getPlayerView());
                }
            }
        }
        new NoReplyMessage("After your moves: " + player.getDiningRoom()).send(pc.getPlayerView());
    }

    // TODO: 11/05/2022 all the checks that must be done and in a separate method
    public String askWhichAction(int availableActions, PlayerController pc){
        new ActionPhaseMessage(advanced, availableActions,pc.getPlayer(), game.getCharacters()).send(pc.getPlayerView());
        return pc.getPlayerView().getReply();
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
