package it.polimi.ingsw.controller;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.*;
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
    }

    private void bindEntrance(){
            Entrance entrance = player.getEntrance();
            this.entranceController= new EntranceController(player, entrance, playerView);
    }

    public String askPlayerName(List<String> usedNames)  {
        new LoginMessage("Player " + player.getId() + ", enter your nickname:").send(playerView);
        while (true){
            String name = (playerView.getAnswer()).toString();
            if (!usedNames.contains(name)){
                if (name.length() < 20){
                player.setPlayerName(name);
                return name;
            }
                else new StringMessage(Game.ANSI_RED + "Name too long! Insert a shorter name" + Game.ANSI_RESET).send(playerView);
            }
            else new StringMessage(Game.ANSI_RED + "Name already taken! Choose a different name." + Game.ANSI_RESET).send(playerView);
        }
    }

    /**
     * @param remainingColors the remaining colors, by the game controller
     * @return the TowerColor chosen by the player among the remaining ones
     */
    public TowerColor askTowerColor(List<TowerColor> remainingColors) {
        new StringMessage(player.getPlayerName() +
                ", please choose your tower color among the available ones: " + remainingColors).send(playerView);
        while (true) {
            try {
                String input = (playerView.getAnswer()).toString();
                TowerColor choice = TowerColor.valueOf(input.toUpperCase());
                if (remainingColors.contains(choice)){
                    player.setTowerColor(choice);
                    return choice;
                }
            } catch (IllegalArgumentException ex) {
                new StringMessage(Game.ANSI_RED+ "Try again!"+ Game.ANSI_RESET).send(playerView);
            }
            new StringMessage(Game.ANSI_RED+ "Not an acceptable color, available colors are: "
                    + remainingColors.toString()+ Game.ANSI_RESET).send(playerView);
        }
    }
    /**
     *
     * @param remainingWizards the remaining wizards, by askAllforWiz()
     * @return the wizard chosen by the player
     */
    public int askWizard(List<Integer> remainingWizards) {
        new StringMessage(player.getPlayerName() +
                ", choose your wizard number among these: " + remainingWizards).send(playerView);
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
     * asks an assistant as input from those remaining, turns it to false in the map and returns it.
     * This way the GameController will then join all the played assistants and choose the new playerOrder
     */
    public Assistant playAssistant(List<Assistant>playedAssistants){
        ArrayList<Assistant> remass = new ArrayList<>(); //list of remaining assistants
        player.getAssistants().forEach((a,b)->{if(b){remass.add(a);}});

        new PlanningPhaseMessage(remass,playedAssistants,"play one of your remaining assistants: " ).send(playerView);
        while (true) {
            Message input = Message.receive(playerView);
            //String input = new Scanner(System.in).nextLine();
            // TODO: 15/04/2022 would be nice if also putting es.9 or 10 worked
            try {
                Assistant choice = Assistant.valueOf(input.toString().toUpperCase());
                if (!playedAssistants.contains(choice)) {
                    if (remass.contains(choice)) {
                        player.setCurrentAssistant(choice);
                        System.out.println("Current assistant for " + player.getPlayerName() + ": " + choice);
                        player.getAssistants().replace(choice, true, false);
                        return choice;
                    }
                }
                // TODO: 05/05/2022 here would be the place to check if you can only play that one
                else new StringMessage(Game.ANSI_RED+ "That assistant was already played! Try again."+ Game.ANSI_RESET).send(playerView);
            } catch (IllegalArgumentException exception) {
                new StringMessage(Game.ANSI_RED+ "Not a valid assistant, take one from the list: "
                        + remass+ Game.ANSI_RESET).send(playerView);}
        }
    }


    public void playCharacters(List<Character> characters, Game game){
        int chosenChar;
        new PlayCharMessage(characters,player).send(playerView);
        while(true) {
            String str = Message.receive(playerView).toString();
            if (str.equalsIgnoreCase("back")){return;}
            else try{
                chosenChar = Integer.parseInt(str);
                break;
            } catch (Exception ex){new StringMessage("Not a correct number, retry.").send(playerView);}
        }
        Character chara = characters.get(chosenChar-1);
        Character.play(chara,game, this);
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


    public Player getPlayer() {
        return player;
    }

    public EntranceController getEntranceController() {
        return entranceController;
    }

    public VirtualView getPlayerView() {
        return playerView;
    }
}
