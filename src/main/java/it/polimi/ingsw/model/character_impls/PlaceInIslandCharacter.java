package it.polimi.ingsw.model.character_impls;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        String str;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Choose the color of the student: " + students + ", or type \"back\" to annull. ");
                str = scanner.nextLine();
                if (Objects.equals(str, "back")) {
                    break;
                } else {
                    Student stud = Student.valueOf(str.toUpperCase());
                    if (students.contains(stud)) {
                        Island island = player.getEntrance().askWhichIsland(game.getGameMap(), scanner);
                        students.remove(stud);
                        int oldval = island.students.get(stud);
                        island.students.replace(stud, oldval, oldval + 1);
                        students.add(game.drawFromBag());
                        break;
                    } else {
                        System.out.println("the character does not have that student! Try again");
                    }
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Not a valid color, try again.");
            }
        }
        player.getDiningRoom().setCoins(player.getDiningRoom().getCoins() - cost);
    }

    public int getCost() {
        return cost;
    }
}
