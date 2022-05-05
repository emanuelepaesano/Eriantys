package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.*;


public class GameMap implements Serializable {

    private final List<Island> archipelago;
    private int motherNature;
    Random randomizer = new Random();

    public GameMap(){
        archipelago = makeIslands();
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
    public void moveMotherNatureAndCheck(Player player, List<Player> players, int nmoves){
        motherNature = (motherNature+nmoves)%(archipelago.size()); //archipelago changes in size
        Island toCheck = archipelago.get(motherNature);
        Player oldOwner = toCheck.getOwner();
        Player newOwner = toCheck.checkOwner(players);
        if (newOwner!= null && !newOwner.equals(oldOwner)) {doJoins(toCheck);}
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
                tojoin.size += 2;
                archipelago.removeAll(List.of(left,right));
                motherNature = archipelago.indexOf(tojoin);//indices changed
                break;
            case "left":
                tojoin.students.replaceAll((s,i) -> i += left.getStudents().get(s));
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
            string.append(island.getStudents()).append((archipelago.indexOf(island) == motherNature?
                    Game.ANSI_GREEN+" 🍀"+Game.ANSI_RESET:"")).append("\n");

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
    public Island getIslandById(int islandId) {
        List<Island> islands = archipelago.stream()
                .filter(i -> i.id == islandId).toList();
        try {
            return islands.get(0);
        } catch (Exception e) {
            System.out.println("island id: " + islandId + "does not exist.");
        }
        return null;
    }

    public Island askWhichIsland(Scanner scanner){
        while (true){
            System.out.println(
                    "This is the current state of the islands:\n" + this +
                            "\nIndicate the island by its number (0~11):") ;
            try {
                int index = Integer.parseInt(scanner.next());
                if (index>=0 &&index <=11) {
                    return this.getArchipelago().get(index);
                }
                System.out.println("That's not a valid index, please choose one between 0~11.\n");
            }catch (NumberFormatException ex) {
                System.out.println("That's not an index, please choose an index between 0~11.\n");
            }
        }
    }


    public int getMotherNature() {
        return this.motherNature;
    }

}
