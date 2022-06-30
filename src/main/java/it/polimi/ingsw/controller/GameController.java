package it.polimi.ingsw.controller;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.ServerStarter;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;

import java.util.*;

import static it.polimi.ingsw.messages.ActionPhaseMessage.ActionPhaseType.*;

/**
 * Controller for Game
 */
public class GameController {
    private Game game;
    private  List<PlayerController> controllers;
    private List<VirtualView> views;
    VirtualView firstPlayer;
    private Boolean advanced;
    private List<Boolean> playedCharacters;
    private Map<Player,PlayerController> controllerMap;


    /**
     * In the constructor we can put the methods to inizialize the game.
     * Our main will call the constructor of this class to start the game.
     *
     * @param numplayers
     * @param views
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
            try {
                replyToAdvanced(firstPlayer.getReply());
            }catch (DisconnectedException ex){ServerStarter.stopGame(false);}
        }
        if (advanced){
            playedCharacters = new ArrayList<>();
            playedCharacters.add(false);
            playedCharacters.add(false);
            playedCharacters.add(false);
            playedCharacters.add(false);
        }
        askAllPlayerNames();
        askAllForTC(numplayers);
        askAllForWiz();
        game.doSetUp(this.advanced);

    }

    /**
     * The two askStudent methods are used ask the user to choose a Student color.
     * This one is for when the selection happens inside the "schoolView", meaning
     * that Students are selected from the Entrance or the Dining Room.
     * @param player the player model we will send to the user.
     * @param user the virtual view which we will communicate with.
     * @param diningRoom true if selection in dining room, false if in entrance.
     * @return a String which either contains the student name or a control string for the caller
     */
    public static String askStudent(Player player, VirtualView user, boolean diningRoom) throws DisconnectedException{
        String str;
        try{
            if (diningRoom){
                new ActionPhaseMessage(player, selectFromDR).sendAndCheck(user);
            }
            else {new ActionPhaseMessage(player, studselect).sendAndCheck(user);}
            str = user.getReply();
            if (Objects.equals(str, "back")) {return "back";}
            else {
                Student.valueOf(str.toUpperCase());
                return str;
            }
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Not a valid color, try again.");
            return "retry";
        }
    }
    /**
     * This method is the same as the previous one, but it is used for when the communication
     * with the user happens inside the Character View.
     * @param students the list of students contained in the Character.
     * @param indexChar the index of the Character inside the characters list of the game model.
     */
    public static String askStudent(List<Student> students, VirtualView user, int indexChar) throws DisconnectedException{
        String str;
        try{
            new PlayCharMessage(students, indexChar).sendAndCheck(user);
            str = user.getReply();
            if (Objects.equals(str, "back")) {return "back";}
            else {
                Student.valueOf(str.toUpperCase());
                return str;
            }
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Not a valid color, try again.");
            return "retry";
        }

    }

    /**
     *
     * Binds each playerController to their player and view
     */
    private void bindPlayers(){
        controllers = new ArrayList<>();
        controllerMap = new HashMap<>();
        for (Player player: game.getTableOrder()){
            int id = player.getId();
            //We bind the first player to the first view in the list and so on
            PlayerController pc = new PlayerController(player, views.get(id-1));
            controllers.add(pc);
            controllerMap.put(player,pc);
        }
    }


    /**
     * Ask the user (first client) the game mode. (normal or expert)
     */
    public void askForAdvanced(){
        new FirstClientMessage("Normal game or expert version? Please type \"normal\" or \"expert\".").send(firstPlayer);
    }

    public void replyToAdvanced(String choice){
            if (choice.equalsIgnoreCase("expert")) {this.advanced = true;return;}
            else if (choice.equalsIgnoreCase("normal")) {this.advanced = false;return;}
            new NoReplyMessage(true,"Wrong input","","What do you mean? Please type \"normal\" or \"expert\".")
                    .send(firstPlayer);
    }


    /**
     * Ask the user to insert player name.
     */
    private void askAllPlayerNames() {
        List<String> usedNames = new ArrayList<>();
        for (PlayerController pc : controllers){
            String aUsedName= null;
            while(aUsedName == null) {
                pc.askPlayerName();
                String reply = "";
                try {
                    reply = pc.getPlayerView().getReply();
                }catch (DisconnectedException ex){ServerStarter.stopGame(false);}
                aUsedName = pc.replyToPlayerName(reply, usedNames);
            }
            usedNames.add(aUsedName);
        }
    }

    /**
     * Cycles through players and asks them a color.
     * It will be stored as an attribute of the player.
     *
     * @param n the number of players.
     */
    private void askAllForTC(int n){
        ArrayList<TowerColor> remainingColors;
        if (n==3) {remainingColors = new ArrayList<>(Arrays.asList(TowerColor.values()));}
        else{remainingColors = new ArrayList<>(Arrays.asList(TowerColor.WHITE,TowerColor.BLACK));}
        for (PlayerController pc: controllers){
            TowerColor c = null;
            while (c == null) {
                pc.askTowerColor(remainingColors);
                try {
                    String reply = pc.getPlayerView().getReply();
                    c = pc.replyToTowerColor(reply, remainingColors);
                }catch (DisconnectedException ex){ServerStarter.stopGame(false);}
            }
            remainingColors.remove(c);
        }
    }

    /**
     * Cycles through players and asks them a wizard number.
     * It will be stored as an attribute of the player.
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
                }catch (DisconnectedException ex){ServerStarter.stopGame(false);}
                catch (NumberFormatException ignored){}
            }
        }
    }

    /**
     * Main method for the planning phase. Cycles through players following the order specified in the rules,
     * and asks them to play an Assistant. Then a new currentOrder is formed from the results.
     */
    public void doPlanningPhase(){
        //This code is to start from the current first and then go clockwise, following the
        //table order. In this order, we make players play assistants, and store them in a Map
        Map<Integer, Player> Priorities = new TreeMap<>();
        List<Assistant> playedAssistants = new ArrayList<>();
        //this is the index in the tableOrder of current first
        int initialind = game.getTableOrder().indexOf(game.getCurrentOrder().get(0));
        for (int i = initialind; i<initialind+game.numPlayers;i++) {
            PlayerController p = controllers.get(i%game.numPlayers);
            Assistant choice;
            //here we just skip that player if he is disconnected
            try {
                choice = p.playAssistant(playedAssistants);
            }catch (DisconnectedException disconnected ) {continue;}
            playedAssistants.add(choice);
            Priorities.put(choice.getPriority(),p.getPlayer());
        }
        //The second part uses the Map to make a new currentOrder
        List<Player> newOrder = new ArrayList<>();
        int numRep = Priorities.values().size();
        for (int i = 0; i<numRep;i++){
            Player first = Priorities.remove(Collections.min(Priorities.keySet()));
            newOrder.add(first);
        }
        new StringMessage("Player order for this turn:" + newOrder).send(views);
        if (newOrder.size() == 0){ServerStarter.stopGame(false);}
        game.setCurrentOrder(newOrder);
    }


    /**
     * This is the main method for the action phase of each player. It asks the player which action they want to do
     * and then performs the action, until they used all of their moves.
     *
     * @param pc the Player Controller.
     * @throws DisconnectedException if a user disconnects, this Exception will be caught in the ServerApp,
     * and it will skip the turnof that player
     */
    public void doActions(PlayerController pc) throws DisconnectedException {
        Player player = pc.getPlayer();
        EntranceController entranceController = pc.getEntranceController();
        boolean alreadyPlayed = false;
        int availableActions = player.getNumActions();
        while (availableActions>0) {
            String action = askWhichAction(availableActions,pc);
            if (action.equalsIgnoreCase("diningroom")) {
                availableActions -= entranceController.moveToDiningRoom(availableActions, player.getDiningRoom(), game.getTableOrder());
                new ActionPhaseMessage(player,update).sendAndCheck(pc.getPlayerView());
            }
            else if (action.equalsIgnoreCase("islands")){
                int didMove = entranceController.moveToIsland(game.getGameMap());
                if (didMove == 1){
                    new IslandInfoMessage(game, IslandInfoMessage.IslandInfoType.updateMap).sendAndCheck(pc.getPlayerView());
                    availableActions -= didMove;
                }
            }
            else if (action.equalsIgnoreCase("characters")){
                List<Boolean> oldPlayed = new ArrayList<>(playedCharacters);
                if(player.getCoins()!= -1 && !alreadyPlayed){
                    playedCharacters = pc.playCharacters(game.getCharacters(),game, playedCharacters);
                    if (!playedCharacters.equals(oldPlayed)){
                        alreadyPlayed = true;
                    }
                }
                else {
                    new NoReplyMessage(true,"Warning","Already Played","You have already played a character this turn!").send(pc.getPlayerView());
                }
            }
        }
        new ActionPhaseMessage(player,endActions).sendAndCheck(pc.getPlayerView());
    }

    /**
     * Ask which action to take.
     *
     * @param availableActions
     * @param pc
     * @throws DisconnectedException
     */
    public String askWhichAction(int availableActions, PlayerController pc) throws DisconnectedException {
        new ActionPhaseMessage(advanced, availableActions,pc.getPlayer(), game.getCharacters()).sendAndCheck(pc.getPlayerView());
        return pc.getPlayerView().getReply();
    }


    /**
     * Reset the character cards. It's only needed for characters active for a whole turn
     *
     * @param game
     * @param pc
     */
    public void resetCharacters(Game game, PlayerController pc){
        if (advanced) {
            for (int i = 0; i<playedCharacters.size(); i++) {
                if (playedCharacters.get(i)){
                    game.getCharacters().get(i).reset(game, pc);
                    playedCharacters.set(i, false);
                }
            }
        }
    }


    //GETTERS
    public Game getGame() {
        return game;
    }
    public List<PlayerController> getControllers() {
        return controllers;
    }
    public Map<Player, PlayerController> getControllerMap() {
        return controllerMap;
    }
    public List<Boolean> getPlayedCharacters() {
        return playedCharacters;
    }

}
