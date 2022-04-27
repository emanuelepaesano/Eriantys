package it.polimi.ingsw.model.character_impls;

import it.polimi.ingsw.model.*;

import java.util.List;
import java.util.Scanner;

/**
 * For this turn, the chosen color will not count towards influence.
 */
class ZeroPointStudentCharacter extends Characters {
    int cost;

    public ZeroPointStudentCharacter() {
        this.cost = 3;
    }

    public synchronized void play(Player player, Game game) throws InterruptedException {
        if (!Characters.enoughMoney(player,cost)){
            System.out.println("You don't have enough money!");
            return;}
        Student student;
        Scanner scanner = new Scanner(System.in);
        Player thisTurn = game.getCurrentPlayer();
        List<Island> islands = game.getGameMap().getArchipelago();
        while(true) {
            String string = Student.askStudent(List.of(Student.values()), scanner).toUpperCase();
            if (string.equals("RETRY")){continue;}
            if (string.equals("BACK")) {return;}
            else if (List.of(Student.values()).contains(Student.valueOf(string))) {
                student = Student.valueOf(string);
                break;
            }
        }
        Student finalStudent = student;
        List<Integer> oldnumbers = islands.stream().map(i -> i.getStudents().get(finalStudent)).toList();
        this.cost = Characters.payandUpdateCost(player,cost);
        while (game.getCurrentPlayer() == thisTurn) {
            islands.replaceAll(i -> {
                i.getStudents().replace(finalStudent, 0);
                return i;
            });
            System.out.println(game.getGameMap());
            wait();
        }
        islands.replaceAll(i -> {
            i.getStudents().replace(finalStudent, oldnumbers.get(islands.indexOf(i)));
            return i;
        });
        System.out.println(game.getGameMap());
        System.out.println("Thread finished!");
    }

    public int getCost() {
        return cost;
    }
}
