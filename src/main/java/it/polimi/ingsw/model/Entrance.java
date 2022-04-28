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
            System.out.println("For student " +i + " :\n");
            str = Student.askStudent(students, scanner).toUpperCase();
            if  (str.equals("BACK")) {return i;}
            else if  (str.equals("RETRY")) {
                i -= 1;
                continue;}
            else {stud = Student.valueOf(str);}

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
            System.out.println("For student number " +i + " :\n");
            str = Student.askStudent(students, scanner).toUpperCase();
            if  (str.equals("BACK")) {return i;}
            else if  (str.equals("RETRY")) {
                i -= 1;
                continue;}
            else {stud = Student.valueOf(str);}
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


    //only for test, will need to draw from the clouds/bag in the game
    public void fillTEST(){
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



