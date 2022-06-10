package it.polimi.ingsw.controller;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characters.Character;

import java.util.ArrayList;
import java.util.List;

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

    public void askPlayerName()  {
        new LoginMessage("Player " + player.getId() + ", enter your nickname:").send(playerView);
    }
    public String replyToPlayerName(String name, List<String>usedNames){
            if (!usedNames.contains(name)){
                if (name.length() < 20){
                    player.setPlayerName(name);
                    return name;
                }
                else new NoReplyMessage(Game.ANSI_RED + "Name too long! Insert a shorter name" + Game.ANSI_RESET).send(playerView);
                return null;
            }
            else new NoReplyMessage(Game.ANSI_RED + "Name already taken! Choose a different name." + Game.ANSI_RESET).send(playerView);
            return null;
    }

    /**
     * @param remainingColors the remaining colors, by the game controller
     * @return the TowerColor chosen by the player among the remaining ones
     */
    public void askTowerColor(List<TowerColor> remainingColors) {
        new LoginMessage(player.getPlayerName() + ", please choose your tower color among the available ones: " + remainingColors
       , remainingColors).send(playerView);
    }
    public TowerColor replyToTowerColor(String input, List<TowerColor> remainingColors){
            try {
                TowerColor choice = TowerColor.valueOf(input.toUpperCase());
                if (remainingColors.contains(choice)){
                    player.setTowerColor(choice);
                    return choice;
                }
            } catch (IllegalArgumentException ex) {
                new NoReplyMessage(Game.ANSI_RED+ "Try again!"+ Game.ANSI_RESET).send(playerView);
                return null;
            }
            new NoReplyMessage(Game.ANSI_RED+ "Not an acceptable color, available colors are: "
                    + remainingColors+ Game.ANSI_RESET).send(playerView);
            return null;
    }
    /**
     *
     * @param remainingWizards the remaining wizards, by askAllforWiz()
     * @return the wizard chosen by the player
     */
    public void askWizard(List<Integer> remainingWizards) {
        new LoginMessage(remainingWizards,
                player.getPlayerName() + ", choose your wizard number among these: " + remainingWizards).send(playerView);
    }
    public Integer replyToWizard(Integer input, List<Integer> remainingWizards){
        while (true) {
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
        List<Assistant> remass = new ArrayList<>(); //list of remaining assistants
        player.getAssistants().forEach((a,b)->{if(b){remass.add(a);}});

        while (true) {
            new PlanningPhaseMessage(remass,playedAssistants,"play one of your remaining assistants: " ).send(playerView);
            String input = playerView.getReply();
            try {
                Assistant choice = Assistant.valueOf(input.toUpperCase());
                if (!playedAssistants.contains(choice) || playedAssistants.equals(remass)) {
                    if (remass.contains(choice)) {
                        player.setCurrentAssistant(choice);
                        System.out.println("Current assistant for " + player.getPlayerName() + ": " + choice);
                        player.getAssistants().replace(choice, true, false);
                        return choice;
                    }
                }
                else new NoReplyMessage(Game.ANSI_RED+ "That assistant was already played! Try again."+ Game.ANSI_RESET).send(playerView);
            } catch (IllegalArgumentException exception) {
                new NoReplyMessage(Game.ANSI_RED+ "Not a valid assistant, take one from the list: "
                        + remass+ Game.ANSI_RESET).send(playerView);}
        }
    }


    public void playCharacters(List<Character> characters, Game game){
        int chosenChar;
        new PlayCharMessage(characters,player).send(playerView);
        while(true) {
            String str = playerView.getReply();
            if (str.equalsIgnoreCase("back")){return;}
            else try{
                chosenChar = Integer.parseInt(str);
                break;
            } catch (Exception ex){new NoReplyMessage("Not a correct number, retry.").send(playerView);}
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
        new IslandActionMessage(player,possibleMoves).send(playerView);
        while (true) {
            try {
                int choice = Integer.parseInt(playerView.getReply());
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
