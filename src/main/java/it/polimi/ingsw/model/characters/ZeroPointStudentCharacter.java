package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.*;

import java.util.List;

/**
 * For this turn, the chosen color will not count towards influence.
 */
class ZeroPointStudentCharacter extends Character {
    int cost;
    int maxCost;

    public void setChosenStudent(Student chosenStudent) {
        this.chosenStudent = chosenStudent;
    }

    Student chosenStudent;
    List<Island> islands;
    List<Integer> oldnumbers;

    public ZeroPointStudentCharacter() {
        this.cost = 3;
        this.maxCost = 4;
    }

    private void setUp(VirtualView user, Game game){
        Student stud;
        while(true) {
            String string = Student.askStudent(List.of(Student.values()),user,"zeropointchar").toUpperCase();
            if (string.equals("RETRY")){continue;}
            if (string.equals("BACK")) {return;}
            else if (List.of(Student.values()).contains(Student.valueOf(string))) {
                stud = Student.valueOf(string);
                break;
            }
        }
        this.chosenStudent = stud;
    }
/*
    private Student pickStudent(VirtualView user){ //move to controller!
        Student stud;
        while(true) {
            String string = Student.askStudent(List.of(Student.values()),user,"zeropointchar").toUpperCase();
            if (string.equals("RETRY")){continue;}
            if (string.equals("BACK")) {return null;}
            else if (List.of(Student.values()).contains(Student.valueOf(string))) {
                stud = Student.valueOf(string);
                break;
            }
        }
        return stud;
    }
 */
    public synchronized void play(Game game, PlayerController pc){
        Player player = pc.getPlayer();
        if (chosenStudent == null){
            setUp(pc.getPlayerView(), game);
            if (chosenStudent == null){
                return;
            }
        }
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;}
        this.cost = Character.payandUpdateCost(player,cost,maxCost);
        islands = game.getGameMap().getArchipelago();
        oldnumbers = islands.stream().map(i -> i.getStudents().get(chosenStudent)).toList();
        islands.forEach(i -> i.getStudents().replace(chosenStudent, 0));
        System.out.println("Game map for this turn!\n" + game.getGameMap());
    }

    public void reset(Game game, PlayerController pc){
        islands.forEach(i -> i.getStudents().replace(chosenStudent, oldnumbers.get(islands.indexOf(i))));
        islands = null;
        oldnumbers = null;
        chosenStudent = null;
        System.out.println(game.getGameMap());
    }
    public int getCost() {
        return cost;
    }
}
