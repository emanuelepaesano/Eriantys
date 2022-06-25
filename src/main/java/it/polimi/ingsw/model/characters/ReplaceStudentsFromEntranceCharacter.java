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
class ReplaceStudentsFromEntranceCharacter extends Character {
    List<Student> students;
    List<Student> chosenStudents;
    List<Student> chosenStudentsFromEntrance;

    public ReplaceStudentsFromEntranceCharacter(List<Student> students) {
        this.cost = 2;
        this.students = new ArrayList<>(students);
        this.maxCost = 3;
        chosenStudentsFromEntrance = new ArrayList<>(List.of());
        chosenStudents = new ArrayList<>(List.of());
        description = "You may take up to 3 Students from this card and replace them" +
                "with the same number of students from your Entrance.";
    }


    private void pickStudentsFromCharacter(VirtualView user){
        Student student;
        List<Student> studentsCopy = new ArrayList<>(students);
        while (true) {
            new StringMessage("Choose a student from the character to replace it from your Entrance.").send(user);
            String str = Student.askStudent(studentsCopy, user, "replaceStudentsFromEntranceChar").toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else if (List.of(Student.values()).contains(Student.valueOf(str))) {
                student = Student.valueOf(str);
                if (!studentsCopy.contains(student)){
                    new StringMessage(Game.ANSI_RED+"the character does not have that student! Try again"+Game.ANSI_RESET).send(user);
                    continue;
                }
                chosenStudents.add(student);
                studentsCopy.remove(student);
                if (chosenStudents.size()==3){break;}
            }
        }
    }

    private void pickStudentsFromEntrance(VirtualView user, List<Student> entranceStudents){
        Student student;
        while (true) {
            new StringMessage("Choose a student from your entrance to replace it with chosen students.").send(user);
            String str = Student.askStudent(entranceStudents, user, "replaceStudentsFromEntranceChar").toUpperCase();
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
                if (chosenStudentsFromEntrance.size()==chosenStudents.size()){
                    break;
                }
            }
        }
    }
    /**
     * You may take up to 3 students from this card and replace them with the same number of students from your entrance.
     */
    public void play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        List<Student> entranceStudentsCopy = new ArrayList<>(pc.getPlayer().getEntrance().getStudents());
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;
        }
        if (chosenStudents.size() == 0){
            pickStudentsFromCharacter(pc.getPlayerView());
            if (chosenStudents.size() == 0){
                return;
            }
        }
        pickStudentsFromEntrance(pc.getPlayerView(), entranceStudentsCopy);
        if (!(chosenStudentsFromEntrance.size() == chosenStudents.size())){
            new StringMessage("Annulling character play. ").send(pc.getPlayerView());
            chosenStudentsFromEntrance.clear();
            chosenStudents.clear();
            return;
        }
        chosenStudents.forEach(s->students.remove(s));
        students.addAll(chosenStudentsFromEntrance);

        chosenStudentsFromEntrance.forEach(s->pc.getPlayer().getEntrance().getStudents().remove(s));
        pc.getPlayer().getEntrance().getStudents().addAll(chosenStudents);

        player.getDiningRoom().checkProfessors(game.getTableOrder(),false);
        this.cost = Character.payandUpdateCost(player,cost,maxCost);
        System.out.println("New Entrance Room:\n " + player.getEntrance());
        chosenStudents.clear();
        chosenStudentsFromEntrance.clear();
    }



}
