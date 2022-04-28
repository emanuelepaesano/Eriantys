package it.polimi.ingsw.model.characters;

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

    public PlaceInIslandCharacter(Game game) {
        this.cost = 1;
        students = new ArrayList<>(
                List.of(game.drawFromBag(), game.drawFromBag(), game.drawFromBag()));
    }

    public void play(Player player, Game game) {
        //ask the player which one wants and then ask the island
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        Student student;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = Student.askStudent(students, scanner).toUpperCase();
                if (str.equals("RETRY")){continue;}
                if (str.equals("BACK")){return;}
                else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                    student = Student.valueOf(str);
                    if (!students.contains(student)){
                        System.err.println("the character does not have that student! Try again");
                        continue;
                    }
                    break;
                }
            }
        Island island = game.getGameMap().askWhichIsland(scanner);
        students.remove(student);
        int oldval = island.students.get(student);
        island.students.replace(student, oldval, oldval + 1);
        students.add(game.drawFromBag());
        this.cost = Character.payandUpdateCost(player,cost);
    }
    public int getCost() {
        return cost;
    }
}
