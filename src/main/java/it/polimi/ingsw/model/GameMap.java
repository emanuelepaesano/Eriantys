package it.polimi.ingsw.model;

import java.util.*;

public class GameMap {
    //I think we need this class, or we need to
    //put everything to handle the islands in the game
    List<Island> archipelago;

    //make this a singleton class?
    public GameMap(){
        archipelago = makeIslands();
        int startindex = startMotherNature();
        startStudents(startindex);
    }

    /**
     *
     * @return Initializes 12 islands in a list
     */
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
    private int startMotherNature(){

        Random randomizer = new Random();
        int index = randomizer.nextInt(12);
        Island inIsland = archipelago.get(index);
        inIsland.setHasMother(true);
        return index;

    }

    /**
     *
     * @param startindex Needs to know starting motherNature position
     * - initializes students from a "smallBag" containing 2 of each color
     */
    private void startStudents(int startindex){
        System.out.println("mother nature is here:" + startindex);

        ArrayList<StudColor> smallBag = new ArrayList<>(Arrays.asList(StudColor.values()));
        smallBag.addAll(Arrays.asList(StudColor.values())); //these 2 lines make the initial bag

        Random randomizer = new Random();
        //below is the code for student initialization
        for (int i=1;i<12;i++) {
            if (i == 6){continue;}
            int index = randomizer.nextInt(smallBag.size());
            StudColor student = smallBag.remove(index);
            Island island = this.archipelago.get((startindex+i)%12);
            island.students.replace(student,0,1);

        }
    }


    // TODO: 14/04/2022 A nice string representation to show the gameMap in the CLI
    @Override
    public String toString() {
        String string = "";
        for (Island island : archipelago){
            string += "Island " + island.id + ": ";
            string += island.getStudents() + "\n";
        }
        return string;
    }

    public static void main(String[] args) {
        //TEST FOR ISLAND INITIALIZATION
        GameMap gm = new GameMap();
        System.out.println(gm);

    }
}
