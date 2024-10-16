package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.DisconnectedException;
import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.List;

import static it.polimi.ingsw.model.Student.*;

/**
 * Choose a type of Student: every player must return three students of that type from their Dining Room to the bag.
 * if any player has fewer than 3 students of that type, return as many students as they have.
 */
class ReturnThreeStudentsCharacter extends Character {

    Student chosenStudent;

    public ReturnThreeStudentsCharacter() {
        this.cost = 3;
        this.maxCost = 4;
        description="Choose one Student color.\n"+
                "Every player, including yourself, must return 3 Students of that color from the Dining Room to the Bag."+
                " If they have less, they will return as many as they have.";
        this.number = 11;
        this.students = List.of(YELLOW,BLUE,RED,PINK,GREEN);
    }

    private void pickStudent(VirtualView user ,int indexThis) throws DisconnectedException {
        Student student;
        new NoReplyMessage(false,"Play Character","Pick one color",
                "Please select one Student color from the Character.").send(user);
        while (true) {
            String str = GameController.askStudent(List.of(Student.values()), user, indexThis).toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else {
                student = Student.valueOf(str);
                break;
            }
        }
        chosenStudent = student;
    }

    public boolean play(Game game, PlayerController pc) throws DisconnectedException {
        Player player = pc.getPlayer();
        int indexThis = game.getCharacters().indexOf(this);
        if (!Character.enoughMoney(player,cost)){
            Character.sendNoMoneyMessage(pc.getPlayerView());
            return false;
        }

        pickStudent(pc.getPlayerView(), indexThis);
        if (chosenStudent == null){
            Character.sendCancelMessage(pc.getPlayerView());
            return false;
        }

        List<Player> allPlayers = game.getTableOrder();
        allPlayers.forEach(p -> {
            int oldnum = p.getDiningRoom().getTables().get(chosenStudent);
            int newnum = oldnum - 3;
            if (newnum < 0) {newnum = 0;}
            p.getDiningRoom().getTables().replace(chosenStudent, oldnum, newnum);
            game.addToBag(chosenStudent, oldnum-newnum);
        });
        this.cost = Character.payandUpdateCost(player,cost,maxCost);
        return true;

    }

    @Override
    public void reset(Game game, PlayerController pc) {
        chosenStudent = null;
    }
}
