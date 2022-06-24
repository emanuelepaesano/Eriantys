package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.List;

/**
 * Choose a type of Student: every player must return three students of that type from their Dining Room to the bag.
 * if any player has fewer than 3 students of that type, return as many students as they have.
 */
class ReturnThreeStudentsCharacter extends Characters {
    int cost;
    int maxCost;

    Student chosenStudent;

    public ReturnThreeStudentsCharacter() {
        this.cost = 3;
        this.maxCost = 999;
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
        Player player = pc.getPlayer();
        if (!Characters.enoughMoney(player,cost)){
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
            if (newnum < 0) {
                newnum = 0;
            }
            p.getDiningRoom().getTables().replace(chosenStudent, oldnum, newnum);
            game.addToBag(chosenStudent, oldnum-newnum);
        });
        player.getDiningRoom().checkProfessors(game.getTableOrder(),false);
        this.cost = Characters.payandUpdateCost(player,cost,maxCost);
        System.out.println("New Dining Room:\n " + player.getDiningRoom());
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void reset(Game game, PlayerController pc) {
        chosenStudent = null;
    }
}
