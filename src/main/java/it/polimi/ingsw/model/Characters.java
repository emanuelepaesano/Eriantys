package it.polimi.ingsw.model;

public abstract class Characters {

    static Character makeCharacter(Integer chara) {
        //this factory, depending on the subtype of character that is passed,
        //constructs the corresponding character.
        return switch (chara) {
            case 1 -> new CheckProfCharacter();
            case 2 -> new CheckOwnerCharacter();
            case 3 -> new MoreMovementsCharacter();
            case 4 -> new NoTowersCharacter();
            case 5 -> new MoreInfluenceCharacter();
            default -> null;
        };
    }

    void play(Character character, Player p, Game game){
        //this can be called only for characters inside a list of gameCharacter inside a game,
        // probably will be called from player class though

        //every character will have to run in a separate thread because it is active for the whole turn(?)
        Thread activeCharacter = new Thread(()->character.play(p,game));
        activeCharacter.start();
    }
}


class CheckProfCharacter implements Character{
    int cost;

    public CheckProfCharacter() {
        this.cost = 2;

    }
    public void play(Player player, Game game) {
        //for this turn,this player, instead of > only needs >= to win professors.

        this.cost +=1;
    }

    public int getCost() {
        return cost;
    }
}

class CheckOwnerCharacter implements Character {
    int cost;

    public CheckOwnerCharacter() {
        this.cost = 3;
    }
    public void play(Player player, Game game) {
        //for this turn, choose an island to checkOwner() in addition to the normal one


        this.cost +=1;
    }
    public int getCost() {
        return cost;
    }


}
class MoreMovementsCharacter implements Character{
    int cost;

    public MoreMovementsCharacter() {
        this.cost = 1;
    }
    public void play(Player player, Game game) {
        //for this turn, move MN +2 steps

        this.cost +=1;
    }


    public int getCost() {
        return cost;
    }
}

class NoTowersCharacter implements Character{
    int cost;

    public NoTowersCharacter() {
        this.cost = 3;
    }
    public void play(Player player, Game game) {
        //for this turn, for checkowner(), size does not matter :)


        this.cost +=1;
    }
    public int getCost() {
        return cost;
    }
}

class MoreInfluenceCharacter implements Character{
    int cost;

    public MoreInfluenceCharacter() {
        this.cost = 1;
    }
    public void play(Player player, Game game) {
        //this turn +2 influence (n.b. you cant combine characters)
        int thisRound = game.getRound();
        while (game.getRound() == thisRound){
            if(player.getBaseInfluence() ==0){
                player.setBaseInfluence(2);
            }
        }
        player.setBaseInfluence(0);
        this.cost +=1;
    }
    public int getCost() {
        return cost;
    }
}


