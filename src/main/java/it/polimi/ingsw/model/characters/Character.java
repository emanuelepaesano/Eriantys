package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

public interface Character {
     /////////////this factory constructs the corresponding character,
     /////////////depending on the subtype of character that is passed.
     static Character makeCharacter(Integer chara, Game game) {
          return switch (chara) {
               case 1 -> new CheckOwnerCharacter();
               case 2 -> new CheckProfCharacter();
               case 3 -> new MoreInfluenceCharacter();
               case 4 -> new MoreMovementsCharacter();
               case 5 -> new MoveToDRCharacter(game);
               case 6 -> new NoTowersCharacter();
               case 7 -> new PlaceInIslandCharacter(game);
               case 8 -> new ZeroPointStudentCharacter();
               default -> null;
          };
     }

     static void play(Character character, Player p, Game game){
          //this can be called only for characters inside a list of gameCharacter inside a game,
          // probably will be called from player class though

          //every character will have to run in a separate thread because it is active for the whole turn(?)
          Thread activeCharacter = new Thread(()-> {

               try {character.play(p,game);

               } catch (InterruptedException e) {Thread.currentThread().interrupt();}
               System.out.println("Thread finished!");
          });

          activeCharacter.start();
     }

     static Boolean enoughMoney(Player player, int cost) {
          return player.getCoins() >= cost;
     }

     static int payandUpdateCost(Player player, int cost){
          player.setCoins(player.getCoins() - cost);
          System.out.println("coins after payment: " + player.getCoins());
          return cost + 1;
     }

     void play(Player player, Game game) throws InterruptedException;


     int getCost();

}
