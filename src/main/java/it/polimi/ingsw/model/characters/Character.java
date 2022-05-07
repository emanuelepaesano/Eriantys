package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.List;

public interface Character extends Serializable {
     /////////////this factory constructs the corresponding character,
     /////////////depending on the subtype of character that is passed.
     static Character makeCharacter(Integer chara, Game game) {
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
               default -> null;
          };
     }

     static void play(Character character, Game game, PlayerController pc) {
          character.play(game,pc);
     }

     static Boolean enoughMoney(Player player, int cost) {
          return player.getCoins() >= cost;
     }

     static int payandUpdateCost(Player player, int cost){
          player.setCoins(player.getCoins() - cost);
          System.out.println("coins after payment: " + player.getCoins());
          return cost + 1;
     }

     void play(Game game, PlayerController pc);


     int getCost();

}
