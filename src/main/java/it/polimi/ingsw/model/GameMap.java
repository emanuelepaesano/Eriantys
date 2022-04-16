package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;

import java.util.*;

public class GameMap {
    //I think we need this class, or we need to
    //put everything to handle the islands in the game
    List<Island> archipelago;
    Game game;

    //make this a singleton class?
    public GameMap(Game game){
        archipelago = makeIslands();
        this.game = game;
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

    public void moveMotherNature(){
        int nmoves = game.getCurrentPlayer().askMNMoves();
        // Now, take the island where mother nature is true and move n steps to the right inside the gamemap
        int index = searchForMN();
        archipelago.get(index).setHasMother(false);
        archipelago.get((index+nmoves)%12).setHasMother(true);
    }

    private int searchForMN(){
        int index = 0;
        for (Island island : archipelago){
            if (island.hasMother){
                index = archipelago.indexOf(island);
            }
        }
        return index;
    }


    // TODO: 14/04/2022 A nice string representation to show the gameMap in the CLI
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Island island : archipelago){
            string.append("Island ").append(island.id).append(": ");
            string.append(island.getStudents()).append(island.hasMother).append("\n");

        }
        return string.toString();
    }

    public List<Island> getArchipelago() {
        return archipelago;
    }

    public static void main(String[] args) {
        //TEST FOR ISLAND INITIALIZATION AND MOVEMOTHER
        GameController gc = new GameController ();
        gc.doPlanningPhase(gc.getGame()); //not complete yet, only here to set a current assistant
        System.out.println(gc.getGame().getGameMap());
        gc.getGame().getGameMap().moveMotherNature();
        System.out.println(gc.getGame().getGameMap());
        System.out.println("mother nature is now here: " + gc.getGame().getGameMap().searchForMN());


    }
}
