package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * You can take 1 student from this character and move it to an island.
 */
 class PlaceInIslandCharacter extends Character {

    List<Student> students;

    public Student getChosenStudent() {
        return chosenStudent;
    }

    Student chosenStudent;

    public Island getChosenIsland() {
        return chosenIsland;
    }

    public void setChosenIsland(Island chosenIsland) {
        this.chosenIsland = chosenIsland;
    }

    Island chosenIsland;


    public PlaceInIslandCharacter(List<Student>students) {
        this.cost = 1;
        this.students = new ArrayList<>(students);
        this.maxCost = 2;
        description = "Take 1 Student from this card and place it on an Island of your choice." +
                "A new Student will be placed on the card.";

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

    /*
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
*/
    public void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        if(chosenStudent == null || chosenIsland == null){
            setUp(pc, game);
            if(chosenStudent == null || chosenIsland == null){
                return;
            }
        }
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}


        students.remove(chosenStudent);
        int oldval = chosenIsland.getStudents().get(chosenStudent);
        chosenIsland.getStudents().replace(chosenStudent, oldval, oldval + 1);
        students.add(game.drawFromBag());
        this.cost = Character.payandUpdateCost(player,cost,maxCost);
    }

    public void reset (Game game, PlayerController pc){
        chosenIsland = null;
        chosenStudent = null;
    }

    //FOR TESTING ONLY
    //Makes the first student red, and returns it
    public Student getFirstStudent(){
        students.set(0, Student.RED);
        return students.get(0);
    }

    public void setChosenStudent(Student stud){
        chosenStudent = stud;
    }
}
