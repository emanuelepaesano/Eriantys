package it.polimi.ingsw.model;

import java.util.*;

public class Island {
    int id;
    Map<Student,Integer> students;
    Player owner;
    int size;


    public Island(int id){
        this.id = id;
        size = 1;
        students = Student.makeStudents();
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
    //Now this is called only by moveMotherNatureAndCheck and returns a boolean for it to decide whether to join or not
    public Boolean checkOwner(List<Player> players) {
        Map<Player, Integer> influences = new HashMap<>();
        players.forEach((Player p)->influences.put(p,p.calculateInfluence(this)));

        List<Player> ties = new ArrayList<>();
        for (Player p : influences.keySet()) {
            if (Objects.equals(influences.get(p), Collections.max(influences.values()))) {
                ties.add(p);
            }
        }
        if (ties.size() == 1) { //if 2 players tie, we cannot have new owner
            Player newowner = ties.get(0);
            if (newowner != this.owner) { //update tower numbers, join if that is the case
                if (owner != null) {owner.setNumTowers(owner.getNumTowers() + this.size);}
                this.owner = newowner;
                System.out.println("Island " + this.id + "has a new owner: " + newowner) ;
                newowner.setNumTowers(newowner.getNumTowers() - this.size);
                return true;
            }
        }
        return false;
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

    public void setSize(int size) {
        this.size = size;
    }

    public Player getOwner() {
        return owner;
    }

}


