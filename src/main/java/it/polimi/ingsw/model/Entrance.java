package it.polimi.ingsw.model;

import java.util.*;

public class Entrance {
    // TODO: 12/04/2022 missing reference to the player.
    //  actually we may not need it
    private final List<Student> students;
    private final DiningRoom diningRoom;
    private int size;

    public Entrance(Game game, DiningRoom diningRoom){
        size = (game.numPlayers == 3 ? 9:7 );
        this.diningRoom = diningRoom;
        //initialize all entries to null
        students = new ArrayList<>(Arrays.asList(new Student[size]));
        System.out.println("size of entrance array: " + students.size());
        //test, bad, remove
        // we will need to fill the entrance following the rules
        fillRandomTEST();
    }

    /**
     *
     * @param availablemoves the moves that are left to the player, will come from the controller.
     * First try of method for moving students to dining room. It needs to be simplified / broken down.
     * Now better with the 2 separate methods
     */
    public void moveToDiningRoom(int availablemoves){
        //First part: we ask how many students to move, maximum availablemoves
        int nstud = askHowMany(availablemoves);

        //Now we ask to move the students
        Student stud;
        for (int i = 0;i<nstud;i++){
            try{stud = askWhich(i);}
            catch (IllegalArgumentException ex) {
                System.out.println("Not a valid student color, try again");
                i-= 1;
                continue;
            }
            if (students.contains(stud)){
                students.remove(stud);
                int oldnum = diningRoom.getTables().get(stud);
                this.diningRoom.getTables().replace(stud,oldnum,oldnum+1);
            }
            else{
                System.out.println("You don't have this student in your entrance");
                i-=1;
            }
        }
    }

    /**
     *
     * @param gm the game map, it's needed to connect to the islands
     * @param availablemoves the same as movetodiningroom.
     *
     * @returns This method asks the current player which students he wants to move to the islands, then does it by updating
     * the game map (returns nothing).
     *
     */
    public void moveToIsland(GameMap gm, int availablemoves){
        //here we do the same thing but with choosing an island index
        //in the second part. Let's not duplicate code, we need to make a separate method
        //for the first part.
        //Probably also for asking which student to move we need an independent method(done).

        //First part is the same.
        int nstud = askHowMany(availablemoves);
        Scanner scanner = new Scanner(System.in);
        Student stud;
        for (int i = 0;i<nstud;i++){
            try{stud = askWhich(i);}
            catch (IllegalArgumentException ex) {
                System.out.println("Not a valid student color, try again");
                i-= 1;
                continue;
            }
            //In the 2nd part now we move it to the chosen island
            if (students.contains(stud)){
                students.remove(stud);
                System.out.println("To which island do you want to move it?\n" +
                        "This is the current state of the islands\n" + gm +
                        "\nIndicate the island by its number (0-11)") ;
                int index = scanner.nextInt(); //we will need a try-catch
                Island island = gm.getArchipelago().get(index);
                int oldval = island.students.get(stud);
                island.students.replace(stud, oldval,oldval+1);
            }
            else{
                System.out.println("You don't have this student in your entrance");
                i-=1;
            }
        }
    }

    // TODO: 16/04/2022 a fillfromClouds method, where you must now the remaining clouds. Or just see
    //  the current state of the clouds and catch an exception if it's empty
    private void fillFromClouds(Game game){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Fill your entrance from a cloud.\n " + game.getClouds() +
                "\n enter a number from 1 to" + game.getClouds().size() + "to choose the cloud.");
        // TODO: 16/04/2022 for now we only choose a number, we will do a more elaborate way to choose
        while (true) {
            try {
                int choice = scanner.nextInt();
                if ( choice<= game.getClouds().size() && choice >= 1 ){
                    Cloud cloud = game.getClouds().get(choice-1);
                    //add those students to our entrance
                    this.students.addAll(cloud.students);
                    cloud.students.clear();
                    break;
                }
            } catch (IllegalArgumentException ex) {System.out.println("Not a number, try again.");}
        }
    }

    /**
     *
     * @param availablemoves you can move at most this number of students
     * @return asks how many students one wants to move. it's used by movetoDiningRoom and movetoIsland
     */
    private int askHowMany(int availablemoves) {
        Scanner scanner = new Scanner(System.in);
        int nstud;
        while (true) {
            System.out.println("How many students do you want to move? (maximum " + availablemoves+ ")\n" +
                    "To return to action selection, type '0' or 'back' (doesn't work yet)"); //obviously back doesnt work
            String in = scanner.next();
            if (Objects.equals(in, "back")) {
                return 0;
                //Basically it will be a return null; statement, to go back to the caller (doActions)
            }
            try {
                nstud = Integer.parseInt(in);
                if (nstud > availablemoves || nstud<0){System.out.println("nice try :)");}
                else{break;}
            } catch (IllegalArgumentException ex) { System.out.println("not a number, try again");}
        }
        return nstud;
    }

    /**
     *
     * @param number the iteration number
     * @return similarly, it's here mostly not to duplicate code
     */
    private Student askWhich(int number){
        Scanner scanner = new Scanner(System.in);
        System.out.println("choose the color of student number " + (number+1) + " from your entrance:" + students);
        String color = scanner.next();
        return Student.valueOf(color.toUpperCase());
    }

    //only for test, will need to draw from the clouds in the game
    private void fillRandomTEST(){
        Random randomizer = new Random();
        for (int i = 0; i< this.size;i++){
            int ind = randomizer.nextInt(5);
            Student s = Arrays.asList(Student.values()).get(ind);
            students.set(i,s);
        }
    }
    @Override
    public String toString() {
        return "Entrance{" +
                "students: " + students +
                '}';
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Student> getStudents() {
        return students;
    }

    public static void main(String[] args) {
        //TEST FOR MOVETODR, ALSO USES THE FILLRANDOMTEST
        Game game = new Game(3);
        DiningRoom dg = new DiningRoom();
        Entrance e = new Entrance(game, dg);
        System.out.println("for >>>MOVETODININGROOM<<<");
        e.moveToDiningRoom(4); //choose 0 or back to test the other method
        System.out.println("Your table configuration after the moves: " + dg.getTables());
        System.out.println("for >>>MOVETOISLAND<<<");
        e.moveToIsland(game.getGameMap(), 4);
        System.out.println("New archipelago: " + game.getGameMap());
        System.out.println("Before filling from clouds: " + e.students);
        e.fillFromClouds(game);
        System.out.println(e);
    }


    public void moveToDiningRoomOLD(int availablemoves){
        //First part: we ask how many students to move, maximum availablemoves
        int nstud = askHowMany(availablemoves);
        //Now we ask to move the students
        Scanner scanner = new Scanner(System.in);
        for (int i = 0;i<nstud;i++){
            try {
                System.out.println("choose the color of student number " + (i+1) + " from your entrance:" + students);
                String color = scanner.next();
                Student stud = Student.valueOf(color.toUpperCase());
                if (students.contains(stud)){
                    students.remove(stud);
                    int old = diningRoom.getTables().get(stud);
                    this.diningRoom.getTables().replace(stud,old,old+1);
                }
                else{
                    System.out.println("You don't have this student in your entrance");
                    i-=1;
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Not a valid student color, try again");
                i-=1;
            }

        }
    }
}
