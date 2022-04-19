package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;

import java.util.*;

public class GameMap {
    // TODO: 19/04/2022 mother nature could just be a int stored here to indicate her position, it would make more
    //  sense from an o.o.p. point of view
    //I think we need this class, or we need to
    //put everything to handle the islands in the game
    private final List<Island> archipelago;
    private final Game game;
    private int motherNature;

    //make this a singleton class?
    public GameMap(Game game){
        this.game = game;
        archipelago = makeIslands();
        motherNature = startMotherNature();
        startStudents(motherNature);
    }

    /**
     *
     * @return Initializes 12 islands in a list
     */
    private List<Island> makeIslands(){
        List<Island> archipelago = new ArrayList<>();
        for (int i = 0; i<12; i++){
            archipelago.add(new Island(i, game));
        }
        return archipelago;
    }

    /**
     * Take a random index and put motherNature in that island
     */
    private int startMotherNature(){
        Random randomizer = new Random();
        return randomizer.nextInt(12);
    }

    /**
     *
     * @param motherNature Needs to know starting motherNature position
     * - Initializes students from a "smallBag" containing 2 of each color
     */
    private void startStudents(int motherNature){
        System.out.println("mother nature is here:" + motherNature);

        ArrayList<Student> smallBag = new ArrayList<>(Arrays.asList(Student.values()));
        smallBag.addAll(Arrays.asList(Student.values())); //these 2 lines make the initial bag

        Random randomizer = new Random();
        //below is the code for student initialization
        for (int i=1;i<12;i++) {
            if (i == 6){continue;}
            int index = randomizer.nextInt(smallBag.size());
            Student student = smallBag.remove(index);
            Island island = this.archipelago.get((motherNature+i)%12);
            island.students.replace(student,0,1);
        }
    }


    public void moveMotherNature(){
        int nmoves = game.getCurrentPlayer().askMNMoves();
        motherNature = (motherNature+nmoves)%(archipelago.size()); //archipelago changes in size
    }



    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Island island : archipelago){
            string.append("Island ").append(island.id).append(": ");
            string.append("Owner{").append(island.owner).append("} ");
            string.append(island.getStudents()).append((archipelago.indexOf(island) == motherNature? " 🍀":"")).append("\n");


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
        System.out.println("mother nature is now here: " + gc.getGame().getGameMap().motherNature);

    }
}
