package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * You may take up to 3 students from this card and replace them with the same number of students from your entrance.
 */
class ReplaceFromEntranceCharacter extends Character {
    List<Student> chosenStudents;
    List<Student> chosenStudentsFromEntrance;

    public ReplaceFromEntranceCharacter(List<Student> students) {
        this.cost = 2;
        this.students = new ArrayList<>(students);
        this.maxCost = 3;
        chosenStudentsFromEntrance = new ArrayList<>(List.of());
        chosenStudents = new ArrayList<>(List.of());
        description = "You may take up to 3 Students from this card and replace them" +
                " with the same number of students from your Entrance. Click on the \"done\" button" +
                " once you are done choosing.";
        this.number = 9;
    }


    private void pickStudentsFromCharacter(VirtualView user, int indexThis) throws DisconnectedException {
        Student student;
        List<Student> studentsCopy = new ArrayList<>(students);
        while (true) {
            String str = GameController.askStudent(studentsCopy, user,indexThis).toUpperCase();
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

    private void pickStudentsFromEntrance(Player player,VirtualView user) throws DisconnectedException {
        Student student;
        List<Student> entranceStudents = player.getEntrance().getStudents();
        while (true) {
            String str = GameController.askStudent(player, user, false).toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){
                //this will annull the play
                entranceStudents.addAll(chosenStudentsFromEntrance);
                return;
            }
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
    public boolean play(Game game, PlayerController pc) throws DisconnectedException {
        Player player = pc.getPlayer();

        int indexThis = game.getCharacters().indexOf(this);
        if (!Character.enoughMoney(player,cost)){
            Character.sendNoMoneyMessage(pc.getPlayerView());
            return false;
        }
        if (chosenStudents.size() == 0){
            new NoReplyMessage(false,"Play Character","Pick Students to move",
                    "Pick a student from the Character and replace it with one from your Entrance.").send(pc.getPlayerView());
            pickStudentsFromCharacter(pc.getPlayerView(), indexThis);
            if (chosenStudents.size() == 0){
                Character.sendCancelMessage(pc.getPlayerView());
                return false;
            }
        }
        pickStudentsFromEntrance(pc.getPlayer(),pc.getPlayerView());
        if (!(chosenStudentsFromEntrance.size() == chosenStudents.size())){
            chosenStudentsFromEntrance.clear();
            chosenStudents.clear();
            Character.sendCancelMessage(pc.getPlayerView());
            return false;
        }
        chosenStudents.forEach(s->students.remove(s));
        students.addAll(chosenStudentsFromEntrance);

        pc.getPlayer().getEntrance().getStudents().addAll(chosenStudents);

        this.cost = Character.payandUpdateCost(player,cost,maxCost);
        chosenStudents.clear();
        chosenStudentsFromEntrance.clear();
        return true;
    }



}
