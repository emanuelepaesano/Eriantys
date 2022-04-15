package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColor;

import java.util.*;

public class GameController {
    private Game game;//remove, only test
    /**
     * In the constructor we can put the methods to inizialize the game.
     * Our main will call the constructor of this class to start the game
     */
    public GameController() {
        int numplayers = AskForPN();
        List<Player> startingOrder = startPlayersandOrder(numplayers); //initialize players, give a random playerOrder to the game
        Game game = new Game(numplayers, startingOrder); //initializes the game, because it's the first call
        this.game = game; //only for test, remove
        askAllForTC(game);
        askAllForWiz(game);

        // TODO: 11/04/2022 initialize the view(s)



    }

    /**
     *
     * @return Asks and returns the number of players for this game
     */
    private int AskForPN() {
        int input = 0;
        while ((input != 3) && (input != 2)) {
            System.out.println("Enter number of players:");
            input = Integer.parseInt(new Scanner(System.in).nextLine());
        }
        return input;
    }

    /**
     * @return initializes the players (and their tower number), returns a random starting order.
     * This one may be moved to a playerController, or maybe from this we make the playercontroller
     * instead
     */
    private static List<Player> startPlayersandOrder(int numplayers){
        ArrayList<Player> startingOrder = new ArrayList<>();
        for (int i=0; i< numplayers; i++){
            PlayerController pc = new PlayerController(i+1, numplayers);
            pc.player.setNumTowers(numplayers==3 ? 6 : 8);
            startingOrder.add(pc.player);
        }
        Collections.shuffle(startingOrder);
        return startingOrder;
    }



    /**
     * Cycles through players and asks them a color.
     * It will be stored as an attribute of the player
     */
    private static void askAllForTC(Game game){
        int n = game.getNumPlayers();
        ArrayList<TowerColor> remainingColors;
        if (n==3) {
            remainingColors = new ArrayList<>(Arrays.asList(TowerColor.values()));
        }
        else{
            remainingColors = new ArrayList<>(Arrays.asList(TowerColor.WHITE,TowerColor.BLACK));
        }
        for (Player player : game.getCurrentOrder()){
            TowerColor c = player.askTowerColor(remainingColors);
            remainingColors.remove(c);
        }
    }

    /**
     * Cycles through players and asks them a wizard number.
     * It will be stored as an attribute of the player
     */
    public static void askAllForWiz(Game game) {
        ArrayList<Integer> remainingWizards = new ArrayList<>(Arrays.asList(1,2,3,4));
        for (Player player : game.getCurrentOrder()){
            Integer wiz = player.askWizard(remainingWizards);
            remainingWizards.remove(wiz);
        }
    }

    /**
     * I put this here for now but maybe we can do a planningPhaseController.
     * This is a bit complicated, we might break it down somehow
     */
    public void doPlanningPhase(Game g){
        // TODO: 13/04/2022 we miss the case in which the player only has the same assistant left
        //This weird code is to start from the current first and then go clockwise, following the
        //table order. In this order, we make players play assistants, and store them in a Map
        Map<Integer, Player> playedAssistants = new TreeMap<>();
        int initialind = g.getTableOrder().indexOf(g.getCurrentOrder().get(0)); //this is the index in the tableOrder if current first
        for (int i = initialind; i<initialind+g.getNumPlayers();i++) {
            Player p = g.getTableOrder().get(i%g.getNumPlayers());
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
        for (int i = 0; i< g.getNumPlayers();i++){
            Player first = playedAssistants.remove(Collections.min(playedAssistants.keySet()));
            newOrder.add(first);
        }
        System.out.println("Player order for this turn:" + newOrder);
        g.setCurrentOrder(newOrder);
    }

    public static void main(String[] args) {
        // Test for the game controller
        GameController gc = new GameController();
        gc.doPlanningPhase(gc.game);

//        for (Player p : gc.game.getCurrentOrder()){
//            System.out.println(p.getPlayerName() +" has this in the entrance:" + p.getEntrance()
//            + ";\n these in the tables:" + p.getDiningRoom().getTables());
//            System.out.println(p.getNumTowers());
//        }


    }
}
