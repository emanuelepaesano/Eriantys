package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * You may exchange up to 2 students between your Entrance and your Dining Room.
 */

class ExchangeStudentsCharacter extends Character {
    int cost;
    int maxCost;

    List<Student> chosenStudentsFromEntrance;
    List<Student> chosenStudentsFromDiningRoom;

    public ExchangeStudentsCharacter() {
        this.cost = 1;
        this.maxCost = 2;
        this.chosenStudentsFromEntrance = new ArrayList<>(List.of());
        this.chosenStudentsFromDiningRoom = new ArrayList<>(List.of());
        description = "You may exchange up to 2 Students between your Entrance and your Dining Room.";
    }

    private void pickStudentsFromEntrance(VirtualView user, List<Student> entranceStudents){
        Student student;
        while (true) {
            //al momento il modo per selezionare uno è dire il primo e poi fare back
            new StringMessage("Choose up to 2 students from your entrance. Type back to stop choosing.").send(user);
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
            }
        }
    }

    private void pickStudentsFromDiningRoom(VirtualView user, Map<Student, Integer> diningRoomStudents){
        Student student;
        new StringMessage("Choose up to 2 students from your dining room.").send(user);
        while (true) {
            String str = Student.askStudent((diningRoomStudents.keySet().
                            stream().filter(s->(diningRoomStudents.get(s)>0)).toList()), user,
                    "exchangeStudentsChar").toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                student = Student.valueOf(str);
                if (diningRoomStudents.get(student) == 0){
                    new StringMessage(Game.ANSI_RED+"Your entrance does not have that student! Try again"+
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
    public void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player, cost)){
            System.err.println("You don't have enough money!");
            //ok, we can send a noreply here
            return;
        }
        List<Student> entranceStudents = new ArrayList<>(pc.getPlayer().getEntrance().getStudents());
        pickStudentsFromEntrance(pc.getPlayerView(), entranceStudents);
        if (chosenStudentsFromEntrance.size() == 0) {
            return;
        }
        Map<Student, Integer> diningRoomStudents = new HashMap<>(pc.getPlayer().getDiningRoom().getTables());
        pickStudentsFromDiningRoom(pc.getPlayerView(), diningRoomStudents);
        if (chosenStudentsFromDiningRoom.size() < chosenStudentsFromEntrance.size()) {
            return;
        }

        //empty entrance + diningroom
        chosenStudentsFromEntrance.forEach(s->player.getEntrance().getStudents().remove(s));
        chosenStudentsFromDiningRoom.forEach(s->{
            int oldnum = player.getDiningRoom().getTables().get(s);
            player.getDiningRoom().getTables().replace(s,oldnum-1);
        });

        //fill entrance + diningroom
        player.getEntrance().getStudents().addAll(chosenStudentsFromDiningRoom);
        chosenStudentsFromEntrance.forEach(s->{
            int oldnum = player.getDiningRoom().getTables().get(s);
            player.getDiningRoom().getTables().replace(s,oldnum+1);
        });
        chosenStudentsFromEntrance.clear();
        chosenStudentsFromDiningRoom.clear();
        //ok paghiamo solo alla fine
        this.cost = Character.payandUpdateCost(player, cost, maxCost);
        player.getDiningRoom().checkProfessors(game.getTableOrder(),false);
        System.out.println("New Entrance for" + player + ":\n " + player.getEntrance());
        System.out.println("New Dining Room" + player + ":\n " + player.getDiningRoom().getTables());
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
