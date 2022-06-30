package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.model.*;

import java.util.List;

import static it.polimi.ingsw.model.Student.*;

/**
 * For this turn, the chosen color will not count towards influence.
 */
class ZeroPointStudentCharacter extends Character {

    public void setChosenStudent(Student chosenStudent) {
        this.chosenStudent = chosenStudent;
    }

    Student chosenStudent;
    List<Island> islands;
    List<Integer> oldnumbers;

    public ZeroPointStudentCharacter() {
        this.cost = 3;
        this.maxCost = 4;
        description = "Choose a Student color.\nFor this turn, that color will not count towards influence on Islands.";
        this.number = 8;
        this.students = List.of(YELLOW,BLUE,RED,PINK,GREEN);
    }

    private void setUp(VirtualView user, int indexThis) throws DisconnectedException {
        Student stud;
        while(true) {
            String string = Student.askStudent(List.of(Student.values()),user,indexThis).toUpperCase();
            if (string.equals("RETRY")){continue;}
            if (string.equals("BACK")) {return;}
            else if (List.of(Student.values()).contains(Student.valueOf(string))) {
                stud = Student.valueOf(string);
                break;
            }
        }
        this.chosenStudent = stud;
    }

    public boolean play(Game game, PlayerController pc) throws DisconnectedException {
        Player player = pc.getPlayer();
        int indexThis = game.getCharacters().indexOf(this);
        if (chosenStudent == null){
            new NoReplyMessage(false,"Play Character","Pick one color",
            "Please select one Student color from the Character.").send(pc.getPlayerView());
            setUp(pc.getPlayerView(), indexThis);
            if (chosenStudent == null){
                Character.sendCancelMessage(pc.getPlayerView());
                return false;
            }
        }
        if (!Character.enoughMoney(player,cost)){
            Character.sendNoMoneyMessage(pc.getPlayerView());
            return false;}
        this.cost = Character.payandUpdateCost(player,cost,maxCost);
        islands = game.getGameMap().getArchipelago();
        oldnumbers = islands.stream().map(i -> i.getStudents().get(chosenStudent)).toList();
        islands.forEach(i -> i.getStudents().replace(chosenStudent, 0));
        return true;
    }

    public void reset(Game game, PlayerController pc){
        islands.forEach(i -> i.getStudents().replace(chosenStudent, oldnumbers.get(islands.indexOf(i))));
        islands = null;
        oldnumbers = null;
        chosenStudent = null;
        System.out.println(game.getGameMap());
    }
}
