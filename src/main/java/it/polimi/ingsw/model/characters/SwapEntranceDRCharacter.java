package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.ActionPhaseMessage;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import javafx.application.Platform;

import java.util.*;

import static it.polimi.ingsw.messages.ActionPhaseMessage.ActionPhaseType.update;

/**
 * You may exchange up to 2 students between your Entrance and your Dining Room.
 */

class SwapEntranceDRCharacter extends Character {
    int cost;
    int maxCost;

    List<Student> chosenStudentsFromEntrance;
    List<Student> chosenStudentsFromDiningRoom;

    public SwapEntranceDRCharacter() {
        this.cost = 1;
        this.maxCost = 2;
        this.chosenStudentsFromEntrance = new ArrayList<>(List.of());
        this.chosenStudentsFromDiningRoom = new ArrayList<>(List.of());
        description = "You may exchange up to 2 Students between your Entrance and your Dining Room.";
        this.number = 10;
    }

    private void pickStudentsFromEntrance(Player player,VirtualView user){
        Student student;
        List<Student> entranceStudents = player.getEntrance().getStudents();
        while (true) {
            //al momento il modo per selezionare uno è dire il primo e poi fare back
            new StringMessage("Choose up to 2 students from your entrance. Type back to stop choosing.").send(user);
            String str = Student.askStudent(player, user, false).toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                student = Student.valueOf(str);
                if (!entranceStudents.contains(student)){
                    new StringMessage(Game.ANSI_RED+"Your entrance does not have that student! Try again"+Game.ANSI_RESET).send(user);
                    continue;
                }
                chosenStudentsFromEntrance.add(student);
                entranceStudents.remove(student);
                if (chosenStudentsFromEntrance.size()==2){break;}
            }
        }
    }

    private void pickStudentsFromDiningRoom(VirtualView user, Player player){
        Student student;
        new StringMessage("Choose up to 2 students from your dining room.").send(user);
        Map<Student, Integer> diningRoomStudents = player.getDiningRoom().getTables();
        int numOfDRStuds = diningRoomStudents.values().stream().mapToInt(Integer::intValue).sum();
        if (numOfDRStuds < chosenStudentsFromEntrance.size()){
            new NoReplyMessage("You don't have enough students in your Dining Room!!").send(user);
            return;
        }
        while (true) {
            String str = Student.askStudent(player, user, true);
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else if (List.of(Student.values()).contains(Student.valueOf(str.toUpperCase()))) {
                student = Student.valueOf(str.toUpperCase());
                if (diningRoomStudents.get(student) == 0){
                    new StringMessage(Game.ANSI_RED+"Your dining room does not have that student! Try again"+
                            Game.ANSI_RESET).send(user);
                    continue;
                }
                chosenStudentsFromDiningRoom.add(student);
                int oldVal = diningRoomStudents.get(student);
                diningRoomStudents.replace(student,oldVal,oldVal-1);
                if (chosenStudentsFromEntrance.size() == chosenStudentsFromDiningRoom.size()){break;}
            }
        }
    }

    /**
     * You may exchange up to 2 students between your Entrance and your Dining Room.
     */
    @Override
    public boolean play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        List<Student> entranceStudents = player.getEntrance().getStudents();
        if (!Character.enoughMoney(player, cost)){
            System.err.println("You don't have enough money!");
            //ok, we can send a noreply here
            return false;
        }

        pickStudentsFromEntrance(pc.getPlayer(), pc.getPlayerView());
        if (chosenStudentsFromEntrance.size() == 0) {
            entranceStudents.addAll(chosenStudentsFromEntrance);
            clear();
            return false;
        }

        pickStudentsFromDiningRoom(pc.getPlayerView(), player);
        if (chosenStudentsFromDiningRoom.size() < chosenStudentsFromEntrance.size()) {
            //give back and cancel everything
            entranceStudents.addAll(chosenStudentsFromEntrance);
            chosenStudentsFromDiningRoom.forEach(s->{
                int oldnum = player.getDiningRoom().getTables().get(s);
                player.getDiningRoom().getTables().replace(s,oldnum+1);
            });
            clear();
            return false;
        }


        //fill entrance + diningroom
        player.getEntrance().getStudents().addAll(chosenStudentsFromDiningRoom);
        chosenStudentsFromEntrance.forEach(s->{
            int oldnum = player.getDiningRoom().getTables().get(s);
            player.getDiningRoom().getTables().replace(s,oldnum+1);
        });

        clear();
        //ok paghiamo solo alla fine
        this.cost = Character.payandUpdateCost(player, cost, maxCost);
        player.getDiningRoom().checkProfessors(game.getTableOrder(),false);
        System.out.println("New Entrance for" + player + ":\n " + player.getEntrance());
        System.out.println("New Dining Room" + player + ":\n " + player.getDiningRoom().getTables());
        new ActionPhaseMessage(player, update).send(pc.getPlayerView());
        return true;
    }

    private void clear(){
        chosenStudentsFromEntrance.clear();
        chosenStudentsFromDiningRoom.clear();
    }
    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void reset(Game game, PlayerController pc) {
        chosenStudentsFromEntrance = new ArrayList<>(List.of());
        chosenStudentsFromDiningRoom = new ArrayList<>(List.of());
    }

}
