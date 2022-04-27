package it.polimi.ingsw.model.character_impls;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.character_impls.*;

public abstract class Characters implements Character {
    ////this factory, depending on the subtype of character that is passed,
    ////constructs the corresponding character.
    public static Character makeCharacter(Integer chara, Game game) {

        return switch (chara) {
            case 1 -> new CheckProfCharacter();
            case 2 -> new CheckOwnerCharacter();
            case 3 -> new MoreMovementsCharacter();
            case 4 -> new NoTowersCharacter();
            case 5 -> new MoreInfluenceCharacter();
            case 6 -> new ZeroPointStudentCharacter();
            case 7 -> new PlaceInIslandCharacter(game);
            case 8 -> new MoveToDRCharacter(game);
            default -> null;
        };
    }

    public static void play(Character character, Player p, Game game){
        //this can be called only for characters inside a list of gameCharacter inside a game,
        // probably will be called from player class though

        //every character will have to run in a separate thread because it is active for the whole turn(?)
        Thread activeCharacter = new Thread(()-> {

            try {character.play(p,game);

            } catch (InterruptedException e) {throw new RuntimeException(e);}
        });

        activeCharacter.start();
    }
    public static int getCost(Character character){
        return character.getCost();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "; current Cost =" +this.getCost();
    }

}


