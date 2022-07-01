package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PlanningPhaseMessage;
import it.polimi.ingsw.model.Assistant;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

import static it.polimi.ingsw.model.Assistant.*;


public class PlanningPhaseView implements View {

    public Button one;
    public Button two;
    public Button three;
    public Button four;
    public Button five;
    public Button six;
    public Button seven;
    public Button eight;
    public Button nine;
    public Button ten;
    public TextField Assistant_text;
    public Parent planningPhaseRoot;

    Stage stage;
    NetworkHandler nh;

    List<Assistant> allAssistants;
    private List<Assistant> remainingAssistants;
    private List<Assistant> playedByOthers;
    List<Button> buttons;

    public void initialize(){
        disableAllAssistants();
        nh = UIManager.getUIManager().getNh();
        allAssistants = List.of(ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,TEN);
        buttons = List.of(one,two,three,four,five,six,seven,eight,nine,ten);
    }


    public Parent getPlanningPhaseRoot() {
        return planningPhaseRoot;
    }

    @Override
    public void display() {}

    @Override
    public void fillInfo(Message message) {
            PlanningPhaseMessage mess = (PlanningPhaseMessage) message;
            this.remainingAssistants = mess.getRemainingAssistants();
            this.playedByOthers = mess.getPlayedByOthers();
            Platform.runLater(this::bindAssistants);
    }

    private void bindAssistants(){
        for (int i=0; i<10;i++){
            Assistant assistant = allAssistants.get(i);
            if (remainingAssistants.contains(assistant)){
                if (!playedByOthers.contains(assistant)||playedByOthers.equals(remainingAssistants)){
                    buttons.get(i).setDisable(false);
                }
            }
        }
    }

    public void disableAllAssistants(){
        one.setDisable(true);
        two.setDisable(true);
        three.setDisable(true);
        four.setDisable(true);
        five.setDisable(true);
        six.setDisable(true);
        seven.setDisable(true);
        eight.setDisable(true);
        nine.setDisable(true);
        ten.setDisable(true);
    }



    public void send1(ActionEvent actionEvent) {
        nh.sendReply(("one"));
        disableAllAssistants();
    }
    public void send2(ActionEvent actionEvent) {
        nh.sendReply(("two"));
        disableAllAssistants();
    }
    public void send3(ActionEvent actionEvent) {
        nh.sendReply(("three"));
        disableAllAssistants();
    }
    public void send4(ActionEvent actionEvent) {
        nh.sendReply(("four"));
        disableAllAssistants();
    }
    public void send5(ActionEvent actionEvent) {
        nh.sendReply(("five"));
        disableAllAssistants();
    }
    public void send6(ActionEvent actionEvent) {
        nh.sendReply(("six"));
        disableAllAssistants();
    }
    public void send7(ActionEvent actionEvent) {
        nh.sendReply(("seven"));
        disableAllAssistants();
    }
    public void send8(ActionEvent actionEvent) {
        nh.sendReply(("eight"));
        disableAllAssistants();
    }
    public void send9(ActionEvent actionEvent) {
        nh.sendReply(("nine"));
        disableAllAssistants();
    }
    public void send10(ActionEvent actionEvent) {
        nh.sendReply(("ten"));
        disableAllAssistants();
    }
}
