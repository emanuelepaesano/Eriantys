package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.*;

public class Island implements Serializable {
    private final int id;
    public Map<Student,Integer> students;
    public Player owner;
    public int size;
    private Boolean joined;
    private Boolean blocked;


    public Island(int id){
        this.id = id;
        size = 1;
        students = Student.makeStudents();
        joined = false;
        owner = null;
        blocked = false;
    }

    /**
     * Check the owner of this island. This method has to be called when mother nature lands on this.
     *
     * @return the Player who has the most influence in the island.
     */
    public Player checkOwner(List<Player> players) {
        Map<Player, Integer> influences = new HashMap<>();
        Player newOwner = this.owner;
        players.forEach((Player p)->influences.put(p,p.calculateInfluence(this)));

        List<Player> ties = new ArrayList<>();
        for (Player p : influences.keySet()) {
            if (Objects.equals(influences.get(p), Collections.max(influences.values()))) {
                ties.add(p);
            }
        }
        //if 2 players tie, we cannot have new owner
        if (ties.size() == 1 && !ties.get(0).equals(this.owner)) {
            newOwner = ties.get(0);
            if (owner != null) {owner.setNumTowers(owner.getNumTowers() + this.size);}
            this.owner = newOwner;
            newOwner.setNumTowers(newOwner.getNumTowers() - this.size);
        }
        return newOwner;
    }


    //GETTERS SETTERS
    public Map<Student, Integer> getStudents() {
        return students;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public Player getOwner() {
        return owner;
    }
    public void setStudents(Map<Student,Integer> students) {
        this.students = students;
    }
    public int getSize() {return size;}
    public int getId() {
        return id;
    }
    public Boolean isJoined() {
        return joined;
    }
    public void setJoined(Boolean joined) {
        this.joined = joined;
    }
    public Boolean isBlocked() {
        return blocked;
    }
    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }
}


