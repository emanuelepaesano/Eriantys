package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.BlockIslandCharacter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class GameMap implements Serializable {

    private List<Island> archipelago;
    private final List<Island> allIslands;
    private int motherNature;
    Random randomizer = new Random();
    private List<Object> lastJoin;
    private BlockIslandCharacter blockChar;

    public GameMap(){
        archipelago = makeIslands();
        allIslands = new ArrayList<>(archipelago);
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
     *
     * - Initializes students from a "smallBag" containing 2 of each color
     */
    public void startMNAndStudents(){

        motherNature =  randomizer.nextInt(12);
        System.out.println("mother nature is here:" + motherNature);

        ArrayList<Student> smallBag = new ArrayList<>(Arrays.asList(Student.values()));
        smallBag.addAll(Arrays.asList(Student.values())); //these 2 lines make the initial bag

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
    public void moveMotherNatureAndCheck(List<Player> players, int nmoves){
        int startIndex = archipelago.indexOf(getIslandById(motherNature));
        Island newIsland = archipelago.get((startIndex+nmoves)%(archipelago.size()));//archipelago changes in size
        motherNature = newIsland.getId();
        if (newIsland.isBlocked()){
            newIsland.setBlocked(false);
            blockChar.setNumTiles(blockChar.getNumTiles()+1);
            return;
        }
        Player oldOwner = newIsland.getOwner();
        Player newOwner = newIsland.checkOwner(players);
        if (newOwner!= null && !newOwner.equals(oldOwner)) {doJoins(newIsland);}
        System.out.println(this);
    }


//    this should be private?
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
                tojoin.size += left.size + right.size;
                right.setJoined(true);
                left.setJoined(true);
                archipelago.removeAll(List.of(left,right));
                break;
            case "left":
                tojoin.students.replaceAll((s,i) -> i += left.getStudents().get(s));
                tojoin.size += left.size;
                left.setJoined(true);
                archipelago.remove(left);
                break;
            case "right":
                tojoin.students.replaceAll((s,i) -> i += right.students.get(s));
                tojoin.size += right.size;
                right.setJoined(true);
                archipelago.remove(right);
                break;
            default:
                System.err.println("Something went wrong...");
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
                lastJoin = List.of(newisland.getId(), "both");
                return "both";
            }
            lastJoin = List.of(newisland.getId(),"right");
            return "right";
        }
        if (archipelago.get(left).owner == newisland.owner){
            lastJoin = List.of(newisland.getId(),"left");
            return "left";
        }
        lastJoin = null;
        return "none";
    }



    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Island island : archipelago) {
            string.append("Island ").append(island.getId()).append(": ");
            string.append("Size=").append(island.size).append("; ");
            string.append("Owner{").append(island.owner).append("} ");
            string.append(island.getStudents()).append((island.getId() == motherNature ?
                    Game.ANSI_BADGREEN + " 🍀" + Game.ANSI_RESET : ""))
                    .append(island.isBlocked()? Game.ANSI_RED + "\uD83D\uDEAB" + Game.ANSI_RESET:"")
                    .append("\n");

        }
        return string.toString();
    }

    public List<Island> getArchipelago() {
        return archipelago;
    }

//    only for test
    public void setMotherNature(int motherNaturePosition) {
        this.motherNature = motherNaturePosition;
    }

//    return should be an island.
private final Island zeroIsland = new Island(99);
    public Island getIslandById(int islandId) {
        zeroIsland.setSize(0);
        List<Island> islands = archipelago.stream()
                .filter(i -> i.getId() == islandId).toList();
        try {
            return islands.get(0);
        } catch (NullPointerException e) {
            return zeroIsland; //if that id does not exist, then it has been joined
        }
    }




    public List<Object> getLastJoin() {
        return lastJoin;
    }
    public int getMotherNature() {
        return this.motherNature;
    }

    public void setArchipelago(List<Island> archipelago) {
        this.archipelago = archipelago;
    }

    public List<Island> getAllIslands() {
        return allIslands;
    }

    public void setBlockChar(BlockIslandCharacter blockChar) {
        this.blockChar = blockChar;
    }
}
