package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * You can take 1 student from this character and move it to your diningRoom
 */
class MoveToDRCharacter extends Character {

    List<Student> students;

    Student chosenStudent;


    public MoveToDRCharacter(List<Student> students) {
        this.cost = 2;
        this.students = new ArrayList<>(students);
        this.maxCost = 3;
        description="Take one Student from this card and place it in your diningroom. A new student" +
                "will be placed on the card.";

    }

    private void pickStudent(VirtualView user){
        Student student;
        while (true) {
            new StringMessage("Choose 1 student from the character to move to your dining Room.").send(user);
            String str = Student.askStudent(students,user,"movetodrchar").toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                student = Student.valueOf(str);
                if (!students.contains(student)){
                    new StringMessage(Game.ANSI_RED+"the character does not have that student! Try again"+Game.ANSI_RESET).send(user);
                    continue;
                }
                break;
            }
        }
        chosenStudent = student;
    }

    public void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        if (chosenStudent == null){
            pickStudent(pc.getPlayerView());
            if (chosenStudent == null){
                return;
            }
        }
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        students.remove(chosenStudent);
        player.getDiningRoom().putStudent(chosenStudent);
        player.getDiningRoom().checkProfessors(game.getTableOrder(),false);
        if (students.size() < 3) {
            students.add(game.drawFromBag());
        }
        this.cost = Character.payandUpdateCost(player,cost,maxCost);
        System.out.println("New Dining Room:\n " + player.getDiningRoom());
    }


    @Override
    public void reset(Game game, PlayerController pc) {
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

    public Student getChosenStudent(){
        return chosenStudent;
    }
    public List<Student> getStudents(){
        return students;
    }
}
