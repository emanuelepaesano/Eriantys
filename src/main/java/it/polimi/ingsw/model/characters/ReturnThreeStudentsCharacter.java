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
 * Choose a type of Student: every player must return three students of that type from their Dining Room to the bag.
 * if any player has fewer than 3 students of that type, return as many students as they have.
 */
class ReturnThreeStudentsCharacter extends Character {

    Student chosenStudent;

    public ReturnThreeStudentsCharacter() {
        this.cost = 3;
        this.maxCost = 4;
        description="Choose one Student color.\n"+
                "Every player, including yoursel, must return 3 Students of that color from the dining room to the bag."+
                "If they have less, they will return as many as they have.";
        this.number = 11;
        students = new ArrayList<>();
    }

    private void pickStudent(VirtualView user){
        Student student;
        while (true) {
            new StringMessage("Choose a type of Student to return three students of that type " +
                    "from every player's Dining Room.").send(user);
            String str = Student.askStudent(List.of(Student.values()), user,"returnThreeStudentsChar").toUpperCase();
            if (str.equals("RETRY")){continue;}
            if (str.equals("BACK")){return;}
            else {
                student = Student.valueOf(str);
                break;
            }
        }
        chosenStudent = student;
    }

    public void play(Game game, PlayerController pc) {
        //this is good, need to implement messages for pickstudent
        Player player = pc.getPlayer();
        if (!Character.enoughMoney(player,cost)){
            System.err.println("You don't have enough money!");
            return;
        }
        pickStudent(pc.getPlayerView());
        if (chosenStudent == null){
            return;
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

    }

    @Override
    public void reset(Game game, PlayerController pc) {
        chosenStudent = null;
    }
}
