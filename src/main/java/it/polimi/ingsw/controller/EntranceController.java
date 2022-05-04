package it.polimi.ingsw.controller;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.*;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class EntranceController {

    Entrance entrance;
    VirtualView view;

    Player player;
    public EntranceController(Player player , Entrance entrance, VirtualView view) {
        this.entrance = entrance;
        this.view = view;
        this.player = player;
    }





    /**
     * Lets the player choose a cloud and fills the entrance with the students of that cloud
     */
    public void fillFromClouds(List<List<Student>> clouds){
        Scanner scanner = new Scanner(System.in);
        //this must be shown to each player, so maybe make a player.askcloud()
        System.out.println("Fill your entrance from a cloud.\n " + clouds +
                "\n enter a number from 1 to " + clouds.size() + " to choose the cloud.");
        while (true) {
            try {
                int choice = scanner.nextInt();
                if (choice<= clouds.size() && choice >= 1 ){
                    List<Student> cloud = clouds.get(choice-1);
                    if (cloud.isEmpty()){
                        //add those students to our entrance
                        player.getEntrance().getStudents().addAll(cloud);
                        cloud.clear();
                        break;
                    }
                    else{System.out.println("That cloud is empty! Try again.");}
                } else {System.out.printf("Not a valid number, type a number between 1 and %d", clouds.size());}
            } catch (IllegalArgumentException ex) {System.out.println("Not a number, try again.");}
        }
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
        List<Student> students = entrance.getStudents();
        String str;
        Student stud;
        for (int i = 0;i<nstud;i++){
            System.out.println("For student " +(i+1) + " :\n");
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
        List<Student> students = entrance.getStudents();
        String str;
        Student stud;
        for (int i = 0;i<nstud;i++){
            System.out.println("For student number " +(i+1) + " :\n");
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


}
