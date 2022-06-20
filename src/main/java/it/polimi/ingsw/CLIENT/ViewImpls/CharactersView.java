package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PlanningPhaseMessage;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.characters.Characters;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

import static it.polimi.ingsw.model.Assistant.*;


public class CharactersView implements View {

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
    public Button eleven;
    public Button twelve;
    public TextField Assistant_text;
    public Parent charactersRoot;

    Stage stage;
    NetworkHandler nh;

    List<Characters> allCharacters;
    private List<Characters> remainingCharacters;
    private List<Characters> playedByOthers;
    List<Button> buttons;

    public void initialize(){
        disableAllCharacters();
        nh = UIManager.getUIManager().getNh();
        allCharacters = null;
        buttons = List.of(one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve);
    }


    public Parent getCharactersRoot() {
        return charactersRoot;
    }

    @Override
    public void display() {
        Parent root = UIManager.getUIManager().getPlanningPhaseRoot();
        stage = UIManager.getUIManager().getMainWindow();
        Scene sc;
        if (root.getScene() == null) {
            sc = new Scene(root);
        }
        else sc = root.getScene();
        stage.setScene(sc);
        stage.setTitle("Characters");
        stage.sizeToScene();
        stage.show();
    }

    @Override
    public void sendReply() {

    }

//    todo: update the cost of characters
    @Override
    public void fillInfo(Message message) {
//            PlanningPhaseMessage mess = (PlanningPhaseMessage) message;
//            this.remainingCharacters = mess.getRemainingCharacters();
//            this.playedByOthers = mess.getPlayedByOthers();
//            Platform.runLater(this::bindCharacters);
    }

//
    private void bindCharacters(){
//        for (int i=0; i<10;i++){
//            Assistant assistant = allCharacters.get(i);
//            if (remainingCharacters.contains(assistant)){
//                if (!playedByOthers.contains(assistant)||playedByOthers.equals(remainingCharacters)){
//                    buttons.get(i).setDisable(false);
//                }
//            }
//        }
    }

    public void disableAllCharacters(){
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
        eleven.setDisable(true);
        twelve.setDisable(true);
    }

    public void send1(ActionEvent actionEvent) {
        nh.sendReply(("one"));
        disableAllCharacters();
    }
    public void send2(ActionEvent actionEvent) {
        nh.sendReply(("two"));
        disableAllCharacters();
    }
    public void send3(ActionEvent actionEvent) {
        nh.sendReply(("three"));
        disableAllCharacters();
    }
    public void send4(ActionEvent actionEvent) {
        nh.sendReply(("four"));
        disableAllCharacters();
    }
    public void send5(ActionEvent actionEvent) {
        nh.sendReply(("five"));
        disableAllCharacters();
    }
    public void send6(ActionEvent actionEvent) {
        nh.sendReply(("six"));
        disableAllCharacters();
    }
    public void send7(ActionEvent actionEvent) {
        nh.sendReply(("seven"));
        disableAllCharacters();
    }
    public void send8(ActionEvent actionEvent) {
        nh.sendReply(("eight"));
        disableAllCharacters();
    }
    public void send9(ActionEvent actionEvent) {
        nh.sendReply(("nine"));
        disableAllCharacters();
    }
    public void send10(ActionEvent actionEvent) {
        nh.sendReply(("ten"));
        disableAllCharacters();
    }
    public void send11(ActionEvent actionEvent) {
        nh.sendReply(("eleven"));
        disableAllCharacters();
    }
    public void send12(ActionEvent actionEvent) {
        nh.sendReply(("twelve"));
        disableAllCharacters();
    }
}
