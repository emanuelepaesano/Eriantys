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


    public Player checkOwner() {
        //returns who has the most influence in the island
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
            this.owner = newowner;
            return newowner;
        }
    }



    //GETTERS SETTERS

    public Map<Student, Integer> getStudents() {
        return students;
    }
}


