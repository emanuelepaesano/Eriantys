package it.polimi.ingsw.model;

import java.util.*;

public class Island {
    int id;
    Map<Student,Integer> students;
    Player owner;
    int size;
    private Game game;

    /**
     *
     * @return Map of all StudColors to 0. This method is used also by the DiningRoom to initialize, hence it's static.
     */
    public static Map<Student,Integer> makeStudents(){
        HashMap<Student,Integer> studs = new HashMap<>();
        for (Student sc : Student.values()){
            studs.put(sc,0);
        }
        return studs;
    }

    public Island(int id, Game game){
        this.id = id;
        this.game = game;
        size = 1;
        students = makeStudents();
        owner = null;
    }
    // TODO: 21/04/2022 a better implementation of the joins with something like a checkjoinable
    //  that sets a bool to true for the joinable islansd
    //  then the doJoins looks for all islands that are joinable and jons them (?)
    //  -> pros: shorter and more readable methods, less duplication (+points)
    //  -> cons: will maybe need to store a this.joinable attribute (-design?)

    /**
     *
     * @return the Player who has the most influence in the island. Has to be called when mother nature lands on this
     */
    public Player checkOwner() {
        Map<Player, Integer> influences = new HashMap<>();
        game.getTableOrder().forEach((Player p)->influences.put(p,p.calculateInfluence(this)));

        List<Player> ties = new ArrayList<>();
        for (Player p : influences.keySet()) {
            if (Objects.equals(influences.get(p), Collections.max(influences.values()))) {
                ties.add(p);
            }
        }
        if (ties.size() > 1) {//if 2 have same best, no new owner
            return owner;
        } else {
            Player newowner = ties.get(0);
            if (newowner != this.owner) { //update tower numbers, join if that is the case
                if (owner != null) {owner.setNumTowers(owner.getNumTowers() + this.size);}
                this.owner = newowner;
                newowner.setNumTowers(newowner.getNumTowers() - this.size);
                game.getGameMap().doJoins(this);
            }
            return newowner;
        }
    }



    //GETTERS SETTERS

    public Map<Student, Integer> getStudents() {
        return students;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setId(int id) {
        this.id = id;
    }
}


