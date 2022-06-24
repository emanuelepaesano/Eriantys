package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;
import java.io.Serializable;

public abstract class Character implements Serializable {

    int cost;
    int maxCost;

    public static Character makeCharacter(Integer chara, Game game) {
        return switch (chara) {
            case 1 -> new CheckOwnerCharacter();
            case 2 -> new CheckProfCharacter();
            case 3 -> new MoreInfluenceCharacter();
            case 4 -> new MoreMovementsCharacter();
            case 5 -> new MoveToDRCharacter(List.of(game.drawFromBag(),
                    game.drawFromBag(),game.drawFromBag(),game.drawFromBag()));
            case 6 -> new NoTowersCharacter();
            case 7 -> new PlaceInIslandCharacter(List.of(game.drawFromBag(),game.drawFromBag(),game.drawFromBag()));
            case 8 -> new ZeroPointStudentCharacter();
            case 9 -> new ReplaceStudentsFromEntranceCharacter(List.of(game.drawFromBag(), game.drawFromBag(),
                    game.drawFromBag(), game.drawFromBag(), game.drawFromBag(), game.drawFromBag()));
            case 10 -> new ExchangeStudentsCharacter();
            case 11 -> new ReturnThreeStudentsCharacter();
            case 12 -> new BlockIslandCharacter();
            default -> null;
        };
    }

    public void play(Game game, PlayerController pc) {


    }

    static Boolean enoughMoney(Player player, int cost) {
        return player.getCoins() >= cost;
    }

    static int payandUpdateCost(Player player, int cost, int maxCost){
        player.setCoins(player.getCoins() - cost);
        System.out.println("coins after payment: " + player.getCoins());
        if (cost == maxCost){
            return cost;
        }
        return cost + 1;
    }

    public int getCost(){
        return cost;
    };

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "; current Cost =" +this.getCost();
    }

    public void reset(Game game, PlayerController pc){
    };

}


