package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * You may take up to 3 students from this card and replace them with the same number of students from your entrance.
 */
class ReplaceStudentsFromEntranceCharacter extends Characters {
    int cost;
    int maxCost;

    List<Student> students;
    List<Student> chosenStudents;
    List<Student> chosenStudentsFromEntrance;

    public ReplaceStudentsFromEntranceCharacter(List<Student> students) {
        this.cost = 2;
        this.students = new ArrayList<>(students);
        this.maxCost = 3;
    }

    private void pickStudentsFromCharacter(VirtualView user){
        Student student;
        while (true) {
            new StringMessage("Choose a student from the character to replace it from your Entrance.").send(user);
            String str = Student.askStudent(students, user, "replaceStudentsFromEntranceChar").toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            if (str.equals("FINISH")){break;}
            else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                student = Student.valueOf(str);
                if (!students.contains(student)){
                    new StringMessage(Game.ANSI_RED+"the character does not have that student! Try again"+Game.ANSI_RESET).send(user);
                    continue;
                }
                chosenStudents.add(student);
                if (chosenStudents.size()==3){break;}
                continue;
            }
        }
    }

    private void pickStudentsFromEntrance(VirtualView user, List<Student> entranceStudents){
        Student student;
        while (true) {
            new StringMessage("Choose a student from your entrance to replace it with chosen students.").send(user);
            String str = Student.askStudent(students, user, "replaceStudentsFromEntranceChar").toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                student = Student.valueOf(str);
                if (!entranceStudents.contains(student)){
                    new StringMessage(Game.ANSI_RED+"Your entrance does not have that student! Try again"+Game.ANSI_RESET).send(user);
                    continue;
                }
                chosenStudentsFromEntrance.add(student);
                entranceStudents.remove(student);
                if (chosenStudents.size()==0){break;}
                continue;
            }
        }
    }

    public void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        List<Student> entranceStudents = pc.getPlayer().getEntrance().getStudents();
        if (chosenStudents.size() == 0){
            pickStudentsFromCharacter(pc.getPlayerView());
            if (chosenStudents.size() == 0){
                return;
            }
        }
        if (!Characters.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;
        }
        students.remove(chosenStudents);
        pickStudentsFromEntrance(pc.getPlayerView(), entranceStudents);
        students.addAll(chosenStudentsFromEntrance);
        player.getEntrance().getStudents().addAll(chosenStudentsFromEntrance);

        player.getDiningRoom().checkProfessors(game.getTableOrder(),false);
        this.cost = Characters.payandUpdateCost(player,cost,maxCost);
        System.out.println("New Entrance Room:\n " + player.getEntrance());
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void reset(Game game, PlayerController pc) {
        chosenStudents = null;
        chosenStudentsFromEntrance = null;
    }

}
