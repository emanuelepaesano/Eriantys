package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.*;

import java.util.List;

/**
 * You can take 1 student from this character and move it to an island.
 */
 class PlaceInIslandCharacter extends Characters {
    int cost;
    List<Student> students;
    int maxCost;
    Student chosenStudent;
    Island chosenIsland;


    public PlaceInIslandCharacter(List<Student>students) {
        this.cost = 1;
        this.students = students;
        this.maxCost = 2;

    }

    private void setUp(PlayerController pc, Game game){
        Student student;
        VirtualView user = pc.getPlayerView();
        while (true) {
            new StringMessage("Choose 1 student from this character to move to an island.").send(user);
            String str = Student.askStudent(students,user,"placeinislandchar").toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                student = Student.valueOf(str);
                if (!students.contains(student)){
                    new StringMessage(Game.ANSI_RED+ "the character does not have that student! Try again"+ Game.ANSI_RESET).send(user);
                    continue;
                }
                break;
            }
        }
        chosenStudent = student;
        Island island = pc.getEntranceController().askWhichIsland(game.getGameMap());
        chosenIsland = island;
    }

    private Student pickStudent(VirtualView user){
        Student student;
        while (true) {
            new StringMessage("Choose 1 student from this character to move to an island.").send(user);
            String str = Student.askStudent(students,user,"placeinislandchar").toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return null;}
            else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                student = Student.valueOf(str);
                if (!students.contains(student)){
                    new StringMessage(Game.ANSI_RED+ "the character does not have that student! Try again"+ Game.ANSI_RESET).send(user);
                    continue;
                }
                break;
            }
        }
        return student;
    }

    public void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        if(chosenStudent == null || chosenIsland == null){
            setUp(pc, game);
            if(chosenStudent == null || chosenIsland == null){
                return;
            }
        }
        if (!Characters.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}


        students.remove(chosenStudent);
        int oldval = chosenIsland.students.get(chosenStudent);
        chosenIsland.students.replace(chosenStudent, oldval, oldval + 1);
        students.add(game.drawFromBag());
        this.cost = Characters.payandUpdateCost(player,cost,maxCost);
    }
    public int getCost() {
        return cost;
    }

    public void reset (Game game, PlayerController pc){
        chosenIsland = null;
        chosenStudent = null;
    }
}
