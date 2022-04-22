package it.polimi.ingsw.model;

import java.util.*;

public class Entrance {

    private final List<Student> students;
    private final DiningRoom diningRoom;
    private final int size;

    //idk if i like taking 3 parameters. alternatives?
    public Entrance(int numPlayers, DiningRoom diningRoom){
        this.diningRoom = diningRoom;
        this.size = (numPlayers==3? 9:7);
        //initialize all entries to null
        students = new ArrayList<>(Arrays.asList(new Student[size]));
        System.out.println("size of entrance array: " + students.size());
    }

    /**
     *
     * @param availablemoves the moves that are left to the player, will come from the controller.
     *
     * @return Method for moving students to dining room. Returns the number of moves used, for doActions()
     *
     */
    public int moveToDiningRoom(int availablemoves){
        //First part: we ask how many students to move, maximum availablemoves
        int nstud = askHowMany(availablemoves);
        //Now we ask to move the students
        String str;
        Student stud;
        for (int i = 0;i<nstud;i++){
            try{
                str = askWhich(i);
                if (Objects.equals(str, "back")) {return i;}
                else {stud = Student.valueOf(str.toUpperCase());}
            }
            catch (IllegalArgumentException ex) {
                System.out.println("Not a valid student color, try again");
                i-= 1;
                continue;
            }
            if (students.contains(stud)){
                students.remove(stud);
                int oldnum = diningRoom.getTables().get(stud);
                diningRoom.getTables().replace(stud,oldnum,oldnum+1);
                //now this cannot be called from here, but it can from player.
                //So we call it every time after we move students, but for all the tables?
                // ->diningRoom.checkProfessor(stud,);

                //there are some alternatives:
                //->we do it as a method in the game instead, harder to implement
                //->we can check for all students at once
            }
            else{
                System.out.println("You don't have this student in your entrance");
                i-=1;
            }
        }
        return nstud; //doActions() needs this
    }

    /**
     *
     * @param availablemoves the same as movetodiningroom.
     *
     * @return Method for moving students to the islands, updates
     * the game map. Returns number of used moves, for doActions()
     *
     */
    public int moveToIsland(int availablemoves, GameMap gm){
        //here we do the same thing but with choosing an island index
        //in the second part. Let's not duplicate code, we need to make a separate method
        //for the first part.
        //Probably also for asking which student to move we need an independent method(done).
        //First part is the same.
        System.out.println("Current map:\n" + gm);
        int nstud = askHowMany(availablemoves);
        String str;
        Student stud;
        for (int i = 0;i<nstud;i++){
            try{
                str = askWhich(i);
                if (Objects.equals(str, "back")) {return i;} //if player wants back at 1st iteration, we don't remove actions and so on
                else {stud = Student.valueOf(str.toUpperCase());}
            }
            catch (IllegalArgumentException ex) {
                System.out.println("Not a valid student color, try again");
                i-= 1;
                continue;
            }
            //In the 2nd part now we move it to the chosen island
            if (students.contains(stud)){
                students.remove(stud);
                Island island = askWhichIsland(gm);
                int oldval = island.students.get(stud);
                island.students.replace(stud, oldval,oldval+1);
            }
            else{
                System.out.println("You don't have this student in your entrance!");
                i-=1;
            }
        }
        return nstud; //doActions() needs this
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
            System.out.println("How many students do you want to move (maximum " + availablemoves+ ") ?\n" +
                    "To return to action selection, type '0' or 'back'"); //obviously back doesnt work
            String in = scanner.next();
            if (Objects.equals(in, "back")) {
                return 0; //go back to movetox and then to doActions()
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
     *similarly, it's here mostly not to duplicate code
     */
    private String askWhich(int number){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the color of student number " + (number+1) + " from your entrance:\n"
                + students + ", or type \"back\" to change action");
        return scanner.next();
    }

    //only for moveToIsland
    private Island askWhichIsland(GameMap gm){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("To which island do you want to move it?\n" +
                    "This is the current state of the islands:\n" + gm +
                    "\nIndicate the island by its number (0~11):") ;
            try {
                int index = Integer.parseInt(scanner.next()); //we will need a try-catch
                if (index>=0 &&index <=11) {
                    return gm.getArchipelago().get(index);
                }
                System.out.println("That's not a valid index, please choose one between 0~11.\n");
            }catch (NumberFormatException ex) {
                System.out.println("That's not an index, please choose an index between 0~11.\n");
            }
        }
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

    public List<Student> getStudents() {
        return students;
    }

    public static void main(String[] args) {
        //TEST FOR MOVETODR AND P.CHECKPROFESSOR, still uses fillrandomTEST
        Game game = new Game(3);
        //1ST PLAYER
        for(Player p : game.getCurrentOrder()) {
//            game.setCurrentPlayer(p);
//            Entrance e = p.getEntrance();
//            System.out.println(p + ": for >>>MOVETODININGROOM<<<, choose 4 to test fillclouds");
//            e.moveToDiningRoom(4); //choose 0 or back to test the other method
//            System.out.println("Your table configuration after the moves: " + p.getDiningRoom());
//            System.out.println("Before filling from clouds: " + e.students);
//            game.fillFromClouds(p);
//            System.out.println("Entrance after filling: " + e);
//            System.out.println(p.getDiningRoom());
//            System.out.println(p + ": for >>>MOVETOISLAND<<<, choose 4 to test fillclouds");
//            e.moveToIsland(4, game.getGameMap());
//            System.out.println("New archipelago: " + game.getGameMap());
//            p.playAssistant(); //only to enable movemothernature
//            game.getGameMap().moveMotherNatureAndCheck(game.getCurrentPlayer(),game.getTableOrder());
//            System.out.println(game.getGameMap());
            game.setCurrentPlayer(p);
            p.doActions(game.getGameMap(),game.getTableOrder());
        }

        for(Player p : game.getCurrentOrder()) {
            System.out.println(p.getPlayerName() + "'s " + p.getDiningRoom());
        }

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
