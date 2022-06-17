package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * You can take 1 student from this character and move it to an island.
 */
 class PlaceInIslandCharacter extends Characters {
    int cost;
    List<Student> students;


    public PlaceInIslandCharacter(List<Student>students) {
        this.cost = 1;
        this.students = students;

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
        Student chosenStudent = pickStudent(pc.getPlayerView());
        if(chosenStudent ==null){return;}
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}

        Island island = null;
        try{island = pc.getEntranceController().askWhichIsland(game.getGameMap());}
        catch (DisconnectedException ex){}
        students.remove(chosenStudent);
        int oldval = island.students.get(chosenStudent);
        island.students.replace(chosenStudent, oldval, oldval + 1);
        students.add(game.drawFromBag());
        this.cost = Character.payandUpdateCost(player,cost);
    }
    public int getCost() {
        return cost;
    }
}
