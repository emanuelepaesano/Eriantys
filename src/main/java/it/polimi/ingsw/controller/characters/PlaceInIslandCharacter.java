package it.polimi.ingsw.controller.characters;

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
    Scanner scanner = new Scanner(System.in);
    Game game;


    public PlaceInIslandCharacter(Game game) {
        this.game = game;
        this.cost = 1;
        students = new ArrayList<>(
                List.of(game.drawFromBag(), game.drawFromBag(), game.drawFromBag()));
    }

    private Student pickStudent(){
        Student student;
        while (true) {
            System.out.println("Choose 1 student from this character to move to an island.");
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
        if(chosenStudent ==null){return;}
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}

        Island island = game.getGameMap().askWhichIsland(scanner);
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
