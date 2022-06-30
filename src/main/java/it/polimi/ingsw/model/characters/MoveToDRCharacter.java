package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.NoReplyMessage;
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

    Student chosenStudent;

    public MoveToDRCharacter(List<Student> students) {
        this.cost = 2;
        this.students = new ArrayList<>(students);
        System.out.println("students: " + students);
        this.maxCost = 3;
        description="Take one Student from this card and place it in your Dining Room. A new student" +
                " will be placed on the card.";
        this.number = 5;

    }

    private void pickStudent(VirtualView user, int indexThis) throws DisconnectedException {
        Student student;
        while (true) {
            new StringMessage("Choose 1 student from the character to move to your dining Room.").send(user);
            String str = Student.askStudent(students,user,indexThis).toUpperCase();
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
    @Override
    public boolean play(Game game, PlayerController pc) throws DisconnectedException {
        Player player = pc.getPlayer();
        int indexThis = game.getCharacters().indexOf(this);
        if (chosenStudent == null){
            pickStudent(pc.getPlayerView(), indexThis);
            if (chosenStudent == null){
                Character.sendCancelMessage(pc.getPlayerView());
                return false;
            }
        }
        if (!Character.enoughMoney(player,cost)){
            Character.sendNoMoneyMessage(pc.getPlayerView());
            return false;}
        students.remove(chosenStudent);
        player.getDiningRoom().putStudent(chosenStudent);
        player.getDiningRoom().checkOneProfessor(chosenStudent,game.getTableOrder(),false);
        if (students.size() < 4) {
            students.add(game.drawFromBag());
        }
        this.cost = Character.payandUpdateCost(player,cost,maxCost);
        System.out.println("New Dining Room:\n " + player.getDiningRoom());
        return true;
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
}
