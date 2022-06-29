package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.StringMessage;
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

    private void pickStudent(VirtualView user ,int indexThis){
        Student student;
        while (true) {
            new StringMessage("Choose a type of Student to return three students of that type " +
                    "from every player's Dining Room.").send(user);
            String str = Student.askStudent(List.of(Student.values()), user, indexThis).toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else {
                student = Student.valueOf(str);
                break;
            }
        }
        chosenStudent = student;
    }

    public boolean play(Game game, PlayerController pc) {
        Player player = pc.getPlayer();
        int indexThis = game.getCharacters().indexOf(this);
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return false;
        }

        pickStudent(pc.getPlayerView(), indexThis);
        if (chosenStudent == null){
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
        System.out.println("New Dining Room:\n " + player.getDiningRoom());
        return true;

    }

    @Override
    public void reset(Game game, PlayerController pc) {
        chosenStudent = null;
    }
}
