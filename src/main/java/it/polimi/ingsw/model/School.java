package it.polimi.ingsw.model;

import java.util.*;

public class School {

    private DiningRoom diningRoom;
    private Entrance entrance;

    //maybe the entrance can contain a reference to the dining room
    //so we dont pass through player every time. then we make the dining room first
    public School(DiningRoom diningRoom, Entrance entrance){
        this.diningRoom = diningRoom;
        this.entrance = entrance;
    }

    /**
     *
     * @param availablemoves the moves that are left to the player, will come from the controller.
     *
     * @return Method for moving students to dining room. Returns the number of moves used, for doActions()
     *
     */
    public int moveStudFrEntranceToDiningRoom(int availablemoves){
        //First part: we ask how many students to move, maximum availablemoves
        int nstud = askHowManyMoves(availablemoves);
        //Now we ask to move the students
        String str;
        Student stud;
        for (int i = 0;i<nstud;i++){
            try{
                str = this.askWhichColor(i);
                if (Objects.equals(str, "back")) {return i;}
                else {stud = Student.valueOf(str.toUpperCase());}
            }
            catch (IllegalArgumentException ex) {
                System.out.println("Not a valid student color, try again");
                i -= 1;
                continue;
            }
            if (this.entrance.getStudents().contains(stud)){
                this.entrance.getStudents().remove(stud);
                int oldNum = this.diningRoom.getTables().get(stud);
                this.diningRoom.getTables().replace(stud, oldNum, oldNum+1);
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
     * @param availablemoves you can move at most this number of students
     * @return asks how many students one wants to move. it's used by movetoDiningRoom and movetoIsland
     */
    private int askHowManyMoves(int availablemoves) {
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
    private String askWhichColor(int number){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the color of student number " + (number+1) + " from your entrance:\n"
                + this.entrance.getStudents() + ", or type \"back\" to change action");
        return scanner.next();
    }

    public DiningRoom getDiningRoom() {
        return this.diningRoom;
    }

    public Entrance getEntrance() {
        return this.entrance;
    }
}
