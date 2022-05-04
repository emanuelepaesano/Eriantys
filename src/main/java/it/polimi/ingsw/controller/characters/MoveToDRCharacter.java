package it.polimi.ingsw.controller.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * You can take 1 student from this character and move it to your diningRoom
 */
class MoveToDRCharacter extends Characters {
    int cost;

    List<Student> students;
    Game game;

    Scanner scanner = new Scanner(System.in);

    public MoveToDRCharacter(Game game) {
        this.cost = 2;
        students = new ArrayList<>(List.of(game.drawFromBag(), game.drawFromBag(),
                game.drawFromBag(), game.drawFromBag()));
        this.game = game;
    }

    private Student pickStudent(){
        Student student;
        while (true) {
            System.out.println("Choose 1 student from the character to move to your dining Room.");
            String str = Student.askStudent(students, scanner).toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return null;}
            else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                student = Student.valueOf(str);
                if (!students.contains(student)){
                    System.err.println("the character does not have that student! Try again");
                    continue;
                }
                break;
            }
        }
        return student;
    }

    public void play(Player player) {
        Student chosenStudent = pickStudent();
        if (chosenStudent == null){return;}
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        students.remove(chosenStudent);
        player.getDiningRoom().putStudent(chosenStudent);
        player.getDiningRoom().checkProfessors(game.getTableOrder(),false);
        if (students.size() < 3) {
            students.add(game.drawFromBag());
        }
        this.cost = Character.payandUpdateCost(player,cost);
        System.out.println("New Dining Room:\n " + player.getDiningRoom());
    }

    @Override
    public int getCost() {
        return cost;
    }

}
