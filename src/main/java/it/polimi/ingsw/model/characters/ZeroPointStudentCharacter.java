package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.*;

import java.util.List;
import java.util.Scanner;

/**
 * For this turn, the chosen color will not count towards influence.
 */
class ZeroPointStudentCharacter extends Characters {
    int cost;
    Scanner scanner = new Scanner(System.in);

    public ZeroPointStudentCharacter() {
        this.cost = 3;

    }


    private Student pickStudent(){ //move to controller!
        Student stud;
        while(true) {
            String string = Student.askStudent(List.of(Student.values()), scanner).toUpperCase();
            if (string.equals("RETRY")){continue;}
            if (string.equals("BACK")) {return null;}
            else if (List.of(Student.values()).contains(Student.valueOf(string))) {
                stud = Student.valueOf(string);
                break;
            }
        }
        return stud;
    }
    public synchronized void play(Player player, Game game) throws InterruptedException {
        Student student = pickStudent();
        if (student == null){return;}
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        Player thisTurn = game.getCurrentPlayer();
        List<Island> islands = game.getGameMap().getArchipelago();
        List<Integer> oldnumbers = islands.stream().map(i -> i.getStudents().get(student)).toList();
        this.cost = Character.payandUpdateCost(player,cost);
        while (game.getCurrentPlayer() == thisTurn) {
            islands.replaceAll(i -> {
                i.getStudents().replace(student, 0);return i;
            });
            System.out.println("Game map for this turn!\n" + game.getGameMap());
            wait();
        }
        islands.replaceAll(i -> {i.getStudents().replace(student, oldnumbers.get(islands.indexOf(i)));return i;
        });
        System.out.println(game.getGameMap());
    }

    public int getCost() {
        return cost;
    }
}
