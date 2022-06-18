package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * You may exchange up to 2 students between your Entrance and your Dining Room.
 */
class ExchangeStudentsCharacter extends Characters {
    int cost;
    int maxCost;

    List<Student> chosenStudentsFromEntrance;
    List<Student> chosenStudentsFromDiningRoom;

    public ExchangeStudentsCharacter() {
        this.cost = 1;
        this.maxCost = 3;
        this.chosenStudentsFromEntrance = null;
        this.chosenStudentsFromDiningRoom = null;
    }

    private void pickStudentsFromEntrance(VirtualView user, List<Student> entranceStudents){
        Student student;
        while (true) {
            new StringMessage("Choose up to 2 students from your entrance.").send(user);
            String str = Student.askStudent(entranceStudents, user, "exchangeStudentsChar").toUpperCase();
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
                continue;
            }
        }
    }

    private void pickStudentsFromDiningRoom(VirtualView user, Map<Student, Integer> diningRoomStudents){
        Student student;
        while (true) {
            new StringMessage("Choose up to 2 students from your dining room.").send(user);
            String str = Student.askStudent((List<Student>) diningRoomStudents.keySet(), user,
                    "exchangeStudentsChar").toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                student = Student.valueOf(str);
                if (!(diningRoomStudents.get(student) == 0)){
                    new StringMessage(Game.ANSI_RED+"Your entrance does not have that student! Try again"+
                            Game.ANSI_RESET).send(user);
                    continue;
                }
                chosenStudentsFromDiningRoom.add(student);
                diningRoomStudents.remove(student);
                if (chosenStudentsFromEntrance.size() == chosenStudentsFromDiningRoom.size()){break;}
                continue;
            }
        }
    }

    public void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        if (!Characters.enoughMoney(player, cost)){
            System.err.println("You don't have enough money!");
            return;
        }
        List<Student> entranceStudents = pc.getPlayer().getEntrance().getStudents();
        pickStudentsFromEntrance(pc.getPlayerView(), entranceStudents);
        if (chosenStudentsFromEntrance.size() == 0) {
            return;
        }
        Map<Student, Integer> diningRoomStudents = pc.getPlayer().getDiningRoom().getTables();
        pickStudentsFromDiningRoom(pc.getPlayerView(), diningRoomStudents);
        if (chosenStudentsFromEntrance.size() == 0) {
            return;
        }
        player.getEntrance().getStudents().removeAll(chosenStudentsFromEntrance);
        player.getDiningRoom().getTables().replaceAll((student, num) -> {
            if (chosenStudentsFromDiningRoom.contains(student)) {
                num--;
                chosenStudentsFromDiningRoom.remove(student);
            }
            return num;
        });

        player.getEntrance().getStudents().addAll(chosenStudentsFromDiningRoom);
        player.getDiningRoom().getTables().replaceAll((student, num) -> {
            if (chosenStudentsFromEntrance.contains(student)) {
                num++;
                chosenStudentsFromEntrance.remove(student);
            }
            return num;
        });

        player.getDiningRoom().checkProfessors(game.getTableOrder(),false);
        this.cost = Characters.payandUpdateCost(player, cost, maxCost);
        System.out.println("New Entrance Room:\n " + player.getEntrance());
        System.out.println("New Dining Room:\n " + player.getDiningRoom().getTables());
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void reset(Game game, PlayerController pc) {
        chosenStudentsFromEntrance = null;
        chosenStudentsFromDiningRoom = null;
    }

}
