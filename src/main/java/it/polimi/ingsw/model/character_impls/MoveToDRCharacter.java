package it.polimi.ingsw.model.character_impls;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * You can take 1 student from this character and move it to your diningRoom
 */
class MoveToDRCharacter extends Characters {
    int cost;

    List<Student> students;

    public MoveToDRCharacter(Game game) {
        this.cost = 2;
        students = new ArrayList<>(List.of(game.drawFromBag(), game.drawFromBag(),
                game.drawFromBag(), game.drawFromBag()));
    }

    public void play(Player player, Game game) {
        player.getDiningRoom().setCoins(player.getDiningRoom().getCoins() - cost);
        Scanner scanner = new Scanner(System.in);
        String str;
        while (true) {
            try {
                System.out.println(player + ", choose 1 student to move to your Dining Room");
                System.out.println("Choose the color of the student: " + students + ", or type \"back\" to annull. ");
                str = scanner.nextLine();
                if (Objects.equals(str, "back")) {
                    return;
                } else {
                    Student stud = Student.valueOf(str.toUpperCase());
                    if (students.contains(stud)) {
                        students.remove(stud);
                        player.getDiningRoom().putStudent(stud);
                        if (students.size() < 3) {
                            students.add(game.drawFromBag());
                        }
                        break;
                    } else {
                        System.out.println("the character does not have that student! Try again");
                    }
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Not a valid color, try again.");
            }
        }
        this.cost += 1;
    }

    @Override
    public int getCost() {
        return cost;
    }

}
