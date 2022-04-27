package it.polimi.ingsw.model;

import java.util.*;

public class Entrance {

    private final List<Student> students;
    private final int size;

    public Entrance(int numPlayers){
        this.size = (numPlayers==3? 9:7);
        //initialize all entries to null
        students = new ArrayList<>(Arrays.asList(new Student[size]));
    }

    /**
     *
     * @param availablemoves the moves that are left to the player, will come from the controller.
     *
     * @return Method for moving students to dining room. Returns the number of moves used, for doActions()
     *
     */
    public int moveToDiningRoom(int availablemoves, DiningRoom diningRoom){
        //First part: we ask how many students to move, maximum availablemoves
        Scanner scanner = new Scanner(System.in);
        int nstud = askHowManyStudents(availablemoves, scanner);
        //Now we ask to move the students
        String str;
        Student stud;
        for (int i = 0;i<nstud;i++){
            switch (str = askWhichColor(i, scanner)){
                case "back": return i;
                case "retry":
                    i-=1;
                    continue;
                default: stud = Student.valueOf(str.toUpperCase());}
            if (students.contains(stud)){
                students.remove(stud);
                diningRoom.putStudent(stud);
            }
            else{
                System.out.println("You don't have this student in your entrance!");
                i-=1;
            }
        }
        return nstud; //doActions() needs this
    }


    public int moveToIsland(int availablemoves, GameMap gm){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Current map:\n" + gm);
        int nstud = askHowManyStudents(availablemoves, scanner);
        String str;
        Student stud;
        for (int i = 0;i<nstud;i++){
            switch (str = askWhichColor(i, scanner)){
                case "back": return i;//if player wants back at 1st iteration, we don't remove actions and so on
                case "retry":
                    i-=1;
                    continue;
                default: stud = Student.valueOf(str.toUpperCase());
            }
            //In the 2nd part now we move it to the chosen island
            if (students.contains(stud)){
                students.remove(stud);
                Island island = gm.askWhichIsland(scanner);
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
     * @param iteration the number of the student moved, from the 2 methods that use this
     * @return a String to signal if the input is acceptable(in this case it's returned), or not
     */
    private String askWhichColor(int iteration, Scanner scanner){
        String str;
        try{
            System.out.println("Choose the color of student number " + (iteration+1) + " from your entrance:\n"
                    + students + ", or type \"back\" to change action");
            str = scanner.nextLine();
            if (Objects.equals(str, "back")) {return "back";}
            else {
                Student.valueOf(str.toUpperCase());
                return str;
            }
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Not a valid color, try again.");
            return ("retry");
        }
    }

    /**
     *
     * @param availablemoves you can move at most this number of students
     * @return asks how many students one wants to move. it's used by movetoDiningRoom and movetoIsland
     */
    private int askHowManyStudents(int availablemoves, Scanner scanner) {
        int nstud;
        while (true) {
            System.out.println("How many students do you want to move (maximum " + availablemoves+ ") ?\n" +
                    "To return to action selection, type '0' or 'back'");
            String in = scanner.nextLine();
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

    //only for moveToIsland





    //only for test, will need to draw from the clouds/bag in the game
    public void fillTEST(){
        Random randomizer = new Random();
        for (int i = 0; i< this.size;i++){
            Student s = Arrays.asList(Student.values()).get(i%5);
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


}



