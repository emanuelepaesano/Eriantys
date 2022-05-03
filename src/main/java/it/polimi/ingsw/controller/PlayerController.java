package it.polimi.ingsw.controller;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characters.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerController {

    private Player player;

    private VirtualView playerView;

    private EntranceController entranceController;

    //Socket client = server.getClients.get(id-1)
    //we have to associate each player with a socket, so we can send messages
    //only to him. so like a method server.update(string,socket) to send only to that
    //as opposed to server.update(string) that sends to all(?)

    public PlayerController(Player player,VirtualView playerView){
        this.player = player;
        this.playerView = playerView;
        bindEntrance();
        player.setPlayerName(askPlayerName());
    }

    private void bindEntrance(){
            Entrance entrance = player.getEntrance();
            this.entranceController= new EntranceController(player, entrance, playerView);
    }

    private String askPlayerName()  {
        playerView.update("Player " + player.getId() + ", enter your nickname:");
        while (true){
            System.out.println("I'm asking the player name!!!!!!!!!!!!!");
            String name = (playerView.getAnswer()).toString();
            if (name.length() < 20){
                return name;
            }
            playerView.update("name too long! Insert a shorter name");
        }
    }

    /**
     * @param remainingColors the remaining colors, by the game controller
     * @return the TowerColor chosen by the player among the remaining ones
     */
    public TowerColor askTowerColor(List<TowerColor> remainingColors) {
        playerView.update(player.getPlayerName() + ", please choose your tower color among the available ones: " + remainingColors);
        while (true) {
            try {
                String input = (playerView.getAnswer()).toString();
                TowerColor choice = TowerColor.valueOf(input.toUpperCase());
                if (remainingColors.contains(choice)){
                    player.setTowerColor(choice);
                    return choice;
                }
            } catch (IllegalArgumentException ex) {
                playerView.update("Try again!");
            }
            playerView.update("Not an acceptable color, available colors are: "+ remainingColors.toString());
        }
    }
    /**
     *
     * @param remainingWizards the remaining wizards, by askAllforWiz()
     * @return the wizard chosen by the player
     */
    public int askWizard(List<Integer> remainingWizards) {
        playerView.update(player.getPlayerName() + ", choose your wizard number among these: " + remainingWizards);
        while (true) {
            int input = Integer.parseInt((playerView.getAnswer()).toString());
            if (remainingWizards.contains(input)){
                Integer wiz = remainingWizards.get(remainingWizards.indexOf(input));
                player.setWizard(wiz);
                return wiz;
            }
        }
    }


    /**
     * This is the main method for the action phase of each player. It asks the player which action they want to do
     * and then performs the action, until they used all of their moves.
     */
    public void doActions(Game game){
        int availableActions = player.getNumActions();
        //this will be an actionlistener linked to 2 buttons. depending on the button pressed
        //(movetodiningroom or movetoisland) the controller calls a different method, then updates model
        while (availableActions>0) {
            String action = askWhichAction(availableActions);
            if (action.equalsIgnoreCase("diningroom")) {
                availableActions -= entranceController.moveToDiningRoom(availableActions, player.getDiningRoom());
                player.getDiningRoom().checkProfessors(game.getTableOrder(),player.isOrEqual());
            }
            else if (action.equalsIgnoreCase("islands")){
                availableActions -= entranceController.moveToIsland(availableActions, game.getGameMap());}
            else if (action.equalsIgnoreCase("characters")){
                if(player.getCoins()!= null){
                    playCharacters(game);
                }
                else {
                    System.out.println("This is not an advanced game!");
                }
            }

        }
        System.out.println("After your moves: " + player.getDiningRoom());
    }

    /**
     * asks an assistant as input from those remaining, turns it to false in the map and returns it.
     * This way the GameController will then join all the played assistants and choose the new playerOrder
     */
    public Assistant playAssistant(){
        ArrayList<Assistant> remass = new ArrayList<>(); //list of remaining assistants
        for (Assistant key: player.getAssistants().keySet()){
            if (player.getAssistants().get(key)){
                remass.add(key);
            }
        }
        System.out.println(", play one of your remaining assistants (speed value): " + remass);
        while (true) {
            String input = new Scanner(System.in).nextLine();
            // TODO: 15/04/2022 would be nice if also putting es.9 or 10 worked
            try {
                Assistant choice = Assistant.valueOf(input.toUpperCase());
                if (remass.contains(choice)){
                    player.setCurrentAssistant(choice);
                    System.out.println("Current assistant for "+ player.getPlayerName() + ": " + choice);
                    player.getAssistants().replace (choice, true, false);
                    return choice;
                }
            } catch (IllegalArgumentException exception) {System.out.println("Not a valid assistant, take one from the list: " + remass);}
        }
    }


    public void playCharacters(Game game){
        List<Character> characters = game.getCharacters();
        // TODO: 03/05/2022 replace with user input!
        Character chara = characters.get(0);
        Character.play(chara,player);
    }

    public String askWhichAction(int availableActions){
        Scanner scanner = new Scanner(System.in);
        System.out.printf("%s, where do you want to move your students (%d moves left)? Please type \"islands\" or \"diningroom\" "
                , player.getPlayerName(),availableActions);
        return scanner.nextLine();
    }

    /**
     *
     * @return The number of steps the player wants to move mother Nature. This method is now only called from GameMap.moveMotherNature().
     * This could change if we choose to move that method
     */
    public int askMNMoves(){
        int possibleMoves = player.getBaseMoves() + player.getCurrentAssistant().getMoves();
        Scanner scanner = new Scanner(System.in);
        System.out.println(player.getPlayerName() + ", how many steps do you want to move Mother Nature? " +
                "(At least 1, maximum " + possibleMoves + ")");
        while (true) {
            try {
                int choice = scanner.nextInt();
                if (choice >=1 && choice<= possibleMoves){
                    return choice;
                }
                else{System.out.println("That choice is not allowed! Try again");}
            } catch (IllegalArgumentException ex) {System.out.println("Not a valid number, try again");}
        }
    }


    public void updatePlayer(Object model){
        playerView.update(model);
    }


    public Player getPlayer() {
        return player;
    }

    public EntranceController getEntranceController() {
        return entranceController;
    }
}
