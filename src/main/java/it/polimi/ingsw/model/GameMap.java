package it.polimi.ingsw.model;

import java.util.*;

public class GameMap {
    //I think we need this class, or we need to
    //put everything to handle the islands in the game
    List<Island> archipelago;

    //make this a singleton class?
    public GameMap(){
        archipelago = makeIslands();
        startMotherNature();
        startStudents();
    }


    private List<Island> makeIslands(){
        List<Island> archipelago = new ArrayList<>();
        for (int i = 0; i<12; i++){
            archipelago.add(new Island(i));
        }
        return archipelago;
    }
    /**
     * Take a random index and put motherNature in that island
     */
    private void startMotherNature(){

        Random randomizer = new Random();
        int index = randomizer.nextInt(12);
        Island inIsland = archipelago.get(index);
        inIsland.setHasMother(true);

    }

    private void startStudents(){
        ArrayList<StudColor> smallBag = new ArrayList<>(Arrays.asList(StudColor.values()));
        smallBag.addAll(Arrays.asList(StudColor.values()));
        System.out.println(smallBag);
        //prendiamo un colore random da towercolor.values
        //poi ne togliamo uno da smallbag e lo mettiamo sulla prossima isola, ma non sulla numero 6
    }

    public static void main(String[] args) {
        new GameMap();
    }
}
