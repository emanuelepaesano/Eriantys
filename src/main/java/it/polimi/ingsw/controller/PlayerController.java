package it.polimi.ingsw.controller;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.*;

import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class PlayerController {

    private Player player;

    private VirtualView playerView;

    //Socket client = server.getClients.get(id-1)
    //we have to associate each player with a socket, so we can send messages
    //only to him. so like a method server.update(string,socket) to send only to that
    //as opposed to server.update(string) that sends to all(?)

    public PlayerController(Player player,VirtualView playerView){
        this.player = player;
        this.playerView = playerView;
        player.setPlayerName(askPlayerName());
    }

    private String askPlayerName()  {
        playerView.update("Player " + player.getId() + ", enter your nickname:");
        while (true){
            System.out.println("I'm asking the player name!!!!!!!!!!!!!");
            String name = playerView.getAnswer();
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
            try {            String input = playerView.getAnswer();
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
            int input = Integer.parseInt(playerView.getAnswer());
            if (remainingWizards.contains(input)){
                Integer wiz = remainingWizards.get(remainingWizards.indexOf(input));
                player.setWizard(wiz);
                return wiz;
            }
        }
    }

    public void updatePlayer(Object model){
        playerView.update(model);
    }


    public Player getPlayer() {
        return player;
    }

}
