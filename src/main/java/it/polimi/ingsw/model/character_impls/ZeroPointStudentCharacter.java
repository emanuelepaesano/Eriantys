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
        Student student;
        Scanner scanner = new Scanner(System.in);
        Player thisTurn = game.getCurrentPlayer();
        List<Island> islands = game.getGameMap().getArchipelago();
        do {
            student = Student.askStudent(List.of(Student.values()), scanner);
        } while (student == null);
        Student finalStudent = student;
        List<Integer> oldnumbers = islands.stream().map(i -> i.getStudents().get(finalStudent)).toList();
        while (game.getCurrentPlayer() == thisTurn) {
            //in tutte le isole zero studenti di quel colore
            //if (islands.stream().map(i->i.getStudents().get(finalStudent)).toList() == oldnumbers)
            islands.replaceAll(i -> {
                i.getStudents().replace(finalStudent, 0);
                return i;
            });
            wait();
        }
        islands.replaceAll(i -> {
            i.getStudents().replace(finalStudent, oldnumbers.get(islands.indexOf(i)));
            return i;
        });
        System.out.println("Thread finished!");
    }

    public int getCost() {
        return cost;
    }
}
