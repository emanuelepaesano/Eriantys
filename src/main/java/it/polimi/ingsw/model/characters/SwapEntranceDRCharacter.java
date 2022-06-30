package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.ActionPhaseMessage;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private void pickStudentsFromEntrance(Player player,VirtualView user) throws DisconnectedException {
        Student student;
        List<Student> entranceStudents = player.getEntrance().getStudents();
        new NoReplyMessage(false,"Character Play","Pick Students to move","Select up to 2 Students, and press \"BACK\"" +
                " when you are done. Then take the same number of Students from your Dining Room.").send(user);
        while (true) {
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

    private boolean pickStudentsFromDiningRoom(VirtualView user, Player player) throws DisconnectedException {
        Student student;
        Map<Student, Integer> diningRoomStudents = player.getDiningRoom().getTables();
        int numOfDRStuds = diningRoomStudents.values().stream().mapToInt(Integer::intValue).sum();
        if (numOfDRStuds < chosenStudentsFromEntrance.size()){
            new NoReplyMessage(true,"Warning","Not enough Students",
            "You don't have enough Students in your Dining Room! Your Coins will be returned.").send(user);
            return false;
        }
        while (true) {
            String str = Student.askStudent(player, user, true);
            if (str.equalsIgnoreCase("RETRY")){continue;}
            if (str.equalsIgnoreCase("BACK")){return true;}
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
        return true;
    }

    /**
     * You may exchange up to 2 students between your Entrance and your Dining Room.
     */
    @Override
    public boolean play(Game game, PlayerController pc) throws DisconnectedException {
        Player player = pc.getPlayer();
        List<Student> entranceStudents = player.getEntrance().getStudents();
        if (!Character.enoughMoney(player, cost)){
            Character.sendNoMoneyMessage(pc.getPlayerView());
            return false;
        }
        pickStudentsFromEntrance(pc.getPlayer(), pc.getPlayerView());
        if (chosenStudentsFromEntrance.size() == 0) {
            entranceStudents.addAll(chosenStudentsFromEntrance);
            clear();
            Character.sendCancelMessage(pc.getPlayerView());
            return false;
        }
        boolean enough = pickStudentsFromDiningRoom(pc.getPlayerView(), player);
        if (chosenStudentsFromDiningRoom.size() < chosenStudentsFromEntrance.size()) {
            entranceStudents.addAll(chosenStudentsFromEntrance);
            chosenStudentsFromDiningRoom.forEach(s->{
                int oldnum = player.getDiningRoom().getTables().get(s);
                player.getDiningRoom().getTables().replace(s,oldnum+1);
            });
            clear();
            if (enough){Character.sendCancelMessage(pc.getPlayerView());}
            new ActionPhaseMessage(player, update).sendAndCheck(pc.getPlayerView());
            return false;
        }
        //fill entrance + diningroom (and check)
        player.getEntrance().getStudents().addAll(chosenStudentsFromDiningRoom);
        chosenStudentsFromEntrance.forEach(s->{
            DiningRoom DR = player.getDiningRoom();
            DR.putStudent(s);
            DR.checkOneProfessor(s,game.getTableOrder(),false);
        });
        clear();
        this.cost = Character.payandUpdateCost(player, cost, maxCost);
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
