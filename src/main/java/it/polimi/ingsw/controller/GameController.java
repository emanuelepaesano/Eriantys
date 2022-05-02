package it.polimi.ingsw.controller;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.*;

import java.util.*;

public class GameController {
    private Game game;

    private  List<PlayerController> controllers;
    private List<VirtualView> views;

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
        bindPlayers(game);
        System.out.println("finito bind players");
        askAllForTC(game);
        askAllForWiz(game);
        Boolean ad = askForAdvanced();
        game.doSetUp(ad);


    }

    /**
     *
     * @return Asks and returns the number of players for this game
     */
    private int askForPN() {
        int input = 0;
        while ((input != 3) && (input != 2)) {
        }
        return input;
    }

    private void bindPlayers(Game game){
        controllers = new ArrayList<>();
        for (Player player: game.getTableOrder()){
            int id = player.getId();
            //We bind the first player to the first view in the list and so on
            controllers.add(new PlayerController(player, views.get(id-1)));
        }
    }
    Boolean askForAdvanced(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Normal game or expert version? Please type \"normal\" or \"expert\".");
        while (true){
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("expert")) {return true;}
            else if (choice.equalsIgnoreCase("normal")) {return false;}

            System.err.println("What do you mean? Please type \"normal\" or \"expert\".");
        }
    }


// TODO: 15/04/2022 maybe these methods should go in the game, not in the controller
//  ("the model is something you ask questions to...?")
    /**
     * Cycles through players and asks them a color.
     * It will be stored as an attribute of the player
     */
    private void askAllForTC(Game game){
        int n = game.numPlayers;
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
    public void askAllForWiz(Game game) {
        ArrayList<Integer> remainingWizards = new ArrayList<>(Arrays.asList(1,2,3,4));
        for (Player player : game.getCurrentOrder()){
            Integer wiz = player.askWizard(remainingWizards);
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
        Map<Integer, Player> playedAssistants = new TreeMap<>();
        int initialind = g.getTableOrder().indexOf(g.getCurrentOrder().get(0)); //this is the index in the tableOrder of current first
        for (int i = initialind; i<initialind+g.numPlayers;i++) {
            Player p = g.getTableOrder().get(i%g.numPlayers);
            //this print should not really be here as it must be shown to each player
            System.out.println("The other players played " + playedAssistants.keySet() + ", please choose a new assistant.");
            Assistant choice = p.playAssistant();

            //if 2 players try to play the same assistant, we restart this loop iteration
            if (playedAssistants.containsKey(choice.getPriority())){
                //this also should be shown to one player only
                System.out.println("Someone else played that assistant, please choose another one.");
                p.getAssistants().replace(choice,false,true);
                i -= 1;
                continue;
            }
            playedAssistants.put(choice.getPriority(),p);
        }

        //The second part uses the Map to make a new currentOrder
        List<Player> newOrder = new ArrayList<>();
        for (int i = 0; i< g.numPlayers;i++){
            Player first = playedAssistants.remove(Collections.min(playedAssistants.keySet()));
            newOrder.add(first);
        }
        System.out.println("Player order for this turn:" + newOrder);
        g.setCurrentOrder(newOrder);
        // TODO: 16/04/2022 right now this updates the currentorder, but we are not taking care of the current player yet
    }


    // TODO: 02/05/2022 remember to close sockets in the main after calling this
    public void EndGame(String endCondition, List<Player> winner){
        if (winner.size()==1){
            updateAllViews(winner.get(0) + "has won the game by " + endCondition );
            //then the main will need to close sockets and all
        }
        else{
            updateAllViews("The game is a tie! Winners are " + winner);
        }
    }
    public void updateAllViews(Object message) {
        for(VirtualView client: views){
            client.update(message);
        }
    }





    //GETTER
    public Game getGame() {
        return game;
    }
    
}
