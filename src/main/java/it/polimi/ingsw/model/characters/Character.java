package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.List;
import java.io.Serializable;

public abstract class Character implements Serializable {

    int cost;
    int maxCost;
    String description;
    int number;
    List<Student> students;

    public static Character makeCharacter(Integer chara, Game game) {
        return switch (chara) {
            case 1 -> new CheckOwnerCharacter();
            case 2 -> new OrEqualCharacter();
            case 3 -> new MoreInfluenceCharacter();
            case 4 -> new MoreMovementsCharacter();
            case 5 -> new MoveToDRCharacter(List.of(game.drawFromBag(),
                    game.drawFromBag(),game.drawFromBag(),game.drawFromBag()));
            case 6 -> new NoTowersCharacter();
            case 7 -> new PlaceInIslandCharacter(List.of(game.drawFromBag(),game.drawFromBag(),
                    game.drawFromBag(),game.drawFromBag()));
            case 8 -> new ZeroPointStudentCharacter();
            case 9 -> new ReplaceFromEntranceCharacter(List.of(game.drawFromBag(), game.drawFromBag(),
                    game.drawFromBag(), game.drawFromBag(), game.drawFromBag(), game.drawFromBag()));
            case 10 -> new SwapEntranceDRCharacter();
            case 11 -> new ReturnThreeStudentsCharacter();
            case 12 -> new BlockIslandCharacter();
            default -> null;
        };
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

    static void sendCancelMessage(VirtualView view){
        new NoReplyMessage("Cancel","Cancel Character play",
        "The Character play was canceled. You will receive your Coins back.").send(view);
    }

    static void sendNoMoneyMessage(VirtualView view){
        new NoReplyMessage("Warning","Cannot play Character","You don't have enough Coins!\n" +
        "You gain Coins when you put a Student on a Coin symbol in the Dining Room. ").send(view);
    }

    public boolean play(Game game, PlayerController pc) throws DisconnectedException {return false;}

    public int getCost(){
        return cost;
    };

    public String getDescription(){return description;}

    public int getNumber(){return number;}
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "; current Cost =" +this.getCost();
    }

    public void reset(Game game, PlayerController pc){};
    public List<Student> getStudents() {return students;}

}


