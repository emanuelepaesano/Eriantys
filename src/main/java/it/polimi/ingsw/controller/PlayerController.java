package it.polimi.ingsw.controller;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characters.Character;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.messages.PlayCharMessage.PlayCharType.play;

/**
 * Controller for Player
 */
public class PlayerController {

    private Player player;
    private VirtualView playerView;
    private EntranceController entranceController;


    public PlayerController(Player player,VirtualView playerView){
        this.player = player;
        this.playerView = playerView;
        bindEntrance();
    }

    /**
     * Bind the entrance to view.
     */
    private void bindEntrance(){
            Entrance entrance = player.getEntrance();
            this.entranceController= new EntranceController(player, entrance, playerView);
    }

    /**
     * Ask player name.
     */
    public void askPlayerName()  {
        new LoginMessage("Player " + player.getId() + ", enter your nickname:").send(playerView);
    }


    /**
     * Reply to player name.
     * Validate and set the player name.
     *
     * @param name player name
     * @param usedNames player name used by others
     * @return name player name
     */
    public String replyToPlayerName(String name, List<String>usedNames){
        if (!usedNames.contains(name)){
            if (name.length() < 20){
                if (name.length() > 0) {
                    player.setPlayerName(name);
                    return name;
                }
                else new NoReplyMessage("Invalid name","Empty Name","Please insert a nickname to play." ).send(playerView);
                return null;
            }
            else new NoReplyMessage("Invalid name","Long Name","Name too long! Please insert a name shorter than 20 characters." ).send(playerView);
            return null;
        }
        else new NoReplyMessage("Invalid name","Name Taken","Name already taken! Choose a different name.").send(playerView);
        return null;
    }

    /**
     * Ask tower color.
     *
     * @param remainingColors the remaining colors, by the game controller
     */
    public void askTowerColor(List<TowerColor> remainingColors) {
        new LoginMessage(player.getPlayerName() + ", please choose your tower color among the available ones: " + remainingColors
       , remainingColors).send(playerView);
    }

    /**
     * Reply the tower color.
     * Validate and set the tower color.
     *
     * @param input tower color chosen
     * @param remainingColors remaining tower colors.
     * @return
     */
    public TowerColor replyToTowerColor(String input, List<TowerColor> remainingColors){
        try {
            TowerColor choice = TowerColor.valueOf(input.toUpperCase());
            if (remainingColors.contains(choice)){
                player.setTowerColor(choice);
                return choice;
            }
        } catch (IllegalArgumentException ex) {
            new NoReplyMessage("Illegal argument","","Please try again.").send(playerView);
            return null;
        }
        new NoReplyMessage("Illegal argument","", "Not an acceptable color, available colors are: "
                + remainingColors).send(playerView);
        return null;
    }

    /**
     * Ask wizard.
     *
     * @param remainingWizards the remaining wizards, by askAllforWiz()
     */
    public void askWizard(List<Integer> remainingWizards) {
        new LoginMessage(remainingWizards,
                player.getPlayerName() + ", choose your wizard number among these: " + remainingWizards).send(playerView);
    }

    /**
     * Reply wizard.
     *
     * @param input wizard chosen
     * @param remainingWizards list of remaining wizards
     * @return
     */
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
     * Asks an assistant as input from those remaining, turns it to false in the map and returns it.
     * This way the GameController will then join all the played assistants and choose the new playerOrder
     *
     * @param playedAssistants
     * @throws DisconnectedException
     */
    public Assistant playAssistant(List<Assistant>playedAssistants) throws DisconnectedException {
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
                else new NoReplyMessage("Invalid Assistant","",Game.ANSI_RED+ "That assistant was already played! Try again."+ Game.ANSI_RESET).send(playerView);
            } catch (IllegalArgumentException exception) {
                new NoReplyMessage("Invalid Assistant","",Game.ANSI_RED+ "Not a valid assistant, take one from the list: "
                        + remass+ Game.ANSI_RESET).send(playerView);}
        }
    }


    /**
     * Play characters.
     *
     * @param characters The list of characters used in the Game.
     * @param game Game
     * @param playedCharacters The list showing which character is played.
     * @return playedCharacters
     * @throws DisconnectedException
     */
    public List<Boolean> playCharacters(List<Character> characters, Game game, List<Boolean> playedCharacters) throws DisconnectedException {
        int chosenChar;
        new PlayCharMessage(characters,player,play).send(playerView);
        while(true) {
            String str = playerView.getReply();
            if (str.equalsIgnoreCase("back")){
                return playedCharacters;
            }
            else try{
                chosenChar = Integer.parseInt(str);
                break;
            } catch (Exception ex){new NoReplyMessage("Invalid number","","Not a correct number, retry.").send(playerView);}
        }
        Character chara = characters.get(chosenChar-1);
        boolean playSuccess = chara.play(game, this);
        if (playSuccess) {
            playedCharacters.set(chosenChar - 1, true);
        }
        return playedCharacters;
    }

    /**
     * Ask how many steps the user wants to move Mother Nature.
     *
     * @return The number of steps the player wants to move mother Nature. This method is now only called from GameMap.moveMotherNature().
     * This could change if we choose to move that method
     */
    public int askMNMoves() throws DisconnectedException {
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

// getters
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
