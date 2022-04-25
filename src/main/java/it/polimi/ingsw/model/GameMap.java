package it.polimi.ingsw.model;

import java.util.*;

public class GameMap {

    private final List<Island> archipelago;
    private int motherNature;

    public GameMap(){
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
            archipelago.add(new Island(i));
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
        System.out.println("mother nature starts here :" + motherNature);

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

    //now this needs to take a list of players. i don't really like it.
    //Splitting it into 2 methods might be a good idea
    public void moveMotherNatureAndCheck(Player player, List<Player> players){
        int nmoves = player.askMNMoves();
        motherNature = (motherNature+nmoves)%(archipelago.size()); //archipelago changes in size
        Island toCheck = archipelago.get(motherNature);
        Player oldOwner = toCheck.getOwner();
        Player newOwner = toCheck.checkOwner(players);
        if (newOwner!= null && !newOwner.equals(oldOwner)) {doJoins(toCheck);}
    }



    //it's quite ugly but it should do the job
    public void doJoins(Island tojoin){
        int startindex = archipelago.indexOf(tojoin);
        Island left = archipelago.get(startindex==0? (archipelago.size()-1):(startindex-1));
        Island right = archipelago.get((startindex+1)%archipelago.size());
        switch (checkJoins(tojoin)){
            case "none":
                break;
            case "both":
                //add all students inside tojoin
                tojoin.students.replaceAll((s,i) -> i += right.students.get(s));
                tojoin.students.replaceAll((s,i) -> i += left.students.get(s));
                tojoin.size += 2;
                archipelago.removeAll(List.of(left,right));
                motherNature = archipelago.indexOf(tojoin);//indices changed
                break;
            case "left":
                tojoin.students.replaceAll((s,i) -> i += left.students.get(s));
                tojoin.size += 1;
                archipelago.remove(left);
                motherNature = archipelago.indexOf(tojoin);
                break;
            case "right":
                tojoin.students.replaceAll((s,i) -> i += right.students.get(s));
                tojoin.size += 1;
                archipelago.remove(right);
                motherNature = archipelago.indexOf(tojoin);
                break;
        }
    }

    /**
     *
     * @param newisland The island from which we start checking. It is the one motherNature lands on
     * @return This method works together with doJoins() to perform the joins in a neighborhood of the input island.
     * The string returned signals to doJoins() which other islands it must be joined with.
     */
    private String checkJoins(Island newisland){
        int index = archipelago.indexOf(newisland);
        int right = ((index+1)%(archipelago.size()));
        int left = (index==0? archipelago.size()-1:(index-1));
        if (archipelago.get(right).owner == newisland.owner){
            if (archipelago.get(left).owner == newisland.owner){
                return "both";
            }
            return "right";
        }
        if (archipelago.get(left).owner == newisland.owner){
            return "left";
        }
        return "none";
    }



    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Island island : archipelago){
            string.append("Island ").append(island.id).append(": ");
            string.append("Size=").append(island.size).append("; ");
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
//        GameController gc = new GameController ();
//        gc.doPlanningPhase(gc.getGame()); //not complete yet, only here to set a current assistant
//        System.out.println(gc.getGame().getGameMap());
//        gc.getGame().getGameMap().moveMotherNatureAndCheck();
//        System.out.println(gc.getGame().getGameMap());
//        System.out.println("mother nature is now here: " + gc.getGame().getGameMap().motherNature);

        //TEST FOR DOJOINS() change the indices as you want
        Game game = new Game(3);
        game.getGameMap().archipelago.get(0).setOwner(game.getCurrentPlayer());
        game.getGameMap().archipelago.get(2).setOwner(game.getCurrentPlayer());
        game.getGameMap().archipelago.get(3).setOwner(game.getCurrentPlayer());
        game.getGameMap().doJoins(game.getGameMap().archipelago.get(3));
        System.out.println(game.getGameMap());
        //now join again the island with size 3
//        game.getGameMap().archipelago.get(9).setOwner(game.getCurrentPlayer());
//        game.getGameMap().archipelago.get(0).setOwner(game.getCurrentPlayer());
//        game.getGameMap().archipelago.get(8).setOwner(game.getCurrentPlayer());
//        game.getGameMap().doJoins(game.getGameMap().archipelago.get(9));
//        System.out.println(game.getGameMap());
    }
}
