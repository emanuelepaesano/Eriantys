package it.polimi.ingsw.controller;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;

import java.util.List;
import java.util.Objects;

/**
 * Controller for Entrance
 */
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
     *  Let the player choose a cloud and fills the entrance with the students of that cloud
     *
     * @param clouds
     * @throws DisconnectedException
     */
    public void fillFromClouds(List<List<Student>> clouds) throws DisconnectedException {
        new NoReplyMessage(false,"Cloud Selection","Select one Cloud",
        "Select one Cloud from the middle. Your Entrance will be filled with the students in it.").send(view);
        while (true) {
            new IslandActionMessage(clouds).sendAndCheck(view);
            try {
                int choice = Integer.parseInt(view.getReply());
                if (choice<= clouds.size() && choice >= 1 ){
                    List<Student> cloud = clouds.get(choice-1);
                    if (!cloud.isEmpty()){
                        player.getEntrance().getStudents().addAll(cloud);
                        cloud.clear();
                        break;
                    }
                    else{new StringMessage(Game.ANSI_RED + "That cloud is empty! Try again." + Game.ANSI_RESET).send(view);}
                } else {new StringMessage(Game.ANSI_RED + "Not a valid number, type a number between 1 and "+ clouds.size()+Game.ANSI_RESET).send(view);}
            } catch (IllegalArgumentException ex) {new StringMessage(Game.ANSI_RED + "Not a number, try again." + Game.ANSI_RESET).send(view);}
        }
    }



    /**
     *
     * @param availablemoves the moves that are left to the player, will come from the controller.
     *
     * @return Method for moving students to dining room. Returns the number of moves used, for doActions()
     *
     */
    public int moveToDiningRoom(int availablemoves, DiningRoom diningRoom, List<Player> players) throws DisconnectedException {
        //First part: we ask how many students to move, maximum availablemoves
        int nstud;
        if (availablemoves>1) {
            nstud = askHowManyStudents(availablemoves);
        }
        else nstud = 1;
        //Now we ask to move the students
        List<Student> students = entrance.getStudents();
        String str;
        Student stud;
        for (int i = 0;i<nstud;i++){

            str = Student.askStudent(player, view, false).toUpperCase();
            if  (str.equals("BACK")) {return i;}
            else if  (str.equals("RETRY")) {i -= 1; continue;}
            else {stud = Student.valueOf(str);}

            if (students.contains(stud)){
                students.remove(stud);
                diningRoom.putStudent(stud);
                player.getDiningRoom().checkOneProfessor(stud,players,player.isOrEqual());
            }
            else{
                System.out.println("You don't have this student in your entrance!");
                i-=1;
            }
        }
        return nstud; //doActions() needs this
    }


    /**
     * @param gm
     * @return
     * @throws DisconnectedException
     */
    public int moveToIsland(GameMap gm) throws DisconnectedException {

        System.out.println("Current map:\n" + gm);
        List<Student> students = entrance.getStudents();
        String str;
        Student stud;
        while(true) {
            str = Student.askStudent(player, view, false).toUpperCase();
            if (str.equals("BACK")) {
                return 0;
            }
            else if (str.equals("RETRY")) {
                continue;
            }
            else {
                stud = Student.valueOf(str);
            }
            //In the 2nd part now we move it to the chosen island
            if (students.contains(stud)){
                Island island = askWhichIsland(gm);
                if (island.getId()==99){return 0;}
                students.remove(stud);
                int oldval = island.students.get(stud);
                island.students.replace(stud, oldval,oldval+1);
                break;
            }
            else{new NoReplyMessage(true,"Invalid Student","","You don't have this student in your entrance!").send(view);}
        }
        return 1; //doActions() needs this
    }



    /**
     *
     * @param availablemoves you can move at most this number of students
     * @return asks how many students one wants to move. it's used by movetoDiningRoom and movetoIsland
     */
    public int askHowManyStudents(int availablemoves) throws DisconnectedException {
        int nstud;
        while (true) {
            new ActionPhaseMessage(player,availablemoves).sendAndCheck(view);
            String in = view.getReply();
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

    public Island askWhichIsland (GameMap gm) throws DisconnectedException {
        while (true){
            new PickIslandMessage(gm).sendAndCheck(view);
            try {
                String str = view.getReply();
                if (str.equalsIgnoreCase("back")){return new Island(99);}
                else {
                    int id = Integer.parseInt(str);
                    if (id >= 0 && id <= 11) {
                        Island island = gm.getIslandById(id);
                        if (island.getId() == 99) {
                            continue;
                        } else return island;
                    }
                }
                new StringMessage(Game.ANSI_RED + "That's not a valid id, please choose one between 0~11.\n"+ Game.ANSI_RESET).send(view);
            }catch (NumberFormatException ex) {
                new StringMessage(Game.ANSI_RED+ "That's not an id, please choose an id between 0~11.\n"+ Game.ANSI_RESET).send(view);
            }
        }
    }


}
