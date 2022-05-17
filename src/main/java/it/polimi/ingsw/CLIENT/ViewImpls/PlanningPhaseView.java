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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


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

    public AnchorPane PlanningPane;

    Stage stage;

    NetworkHandler nh;

    public void initialize(){
        disableAllAssistants();
        nh = UIManager.getUIManager().getNh();
    }

    public void disableAllAssistants(){
        one.setDisable(true); one.setVisible(false);
        two.setDisable(true); two.setVisible(false);
        three.setDisable(true); three.setVisible(false);
        four.setDisable(true); four.setVisible(false);
        five.setDisable(true); five.setVisible(false);
        six.setDisable(true); six.setVisible(false);
        seven.setDisable(true); seven.setVisible(false);
        eight.setDisable(true); eight.setVisible(false);
        nine.setDisable(true); nine.setVisible(false);
        ten.setDisable(true); ten.setVisible(false);
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

    @Override
    public void display() {


    }

    @Override
    public void display(Parent root) {
        stage = UIManager.getUIManager().getMainWindow();
        Scene sc;
        if (root.getScene() == null) {
            sc = new Scene(root);
        }
        else sc = root.getScene();
        stage.setScene(sc);
        stage.setTitle("Eriantys");
        stage.sizeToScene();
        stage.show();

    }

    @Override
    public void sendReply() {

    }

    @Override
    public void fillInfo(Message message) {
        if (message.getClass().getSimpleName().equals("PlanningPhaseMessage")){
            System.out.println("Received message in fillinfo method");
            PlanningPhaseMessage mess = (PlanningPhaseMessage) message;
            System.out.println("Message Cast to PlanningPhaseMessage");
            for (int i = 0; i<mess.getRemainingAssistants().size(); i++){
                System.out.println(mess.getRemainingAssistants().get(i).toString());
            }
            for (int i = 0; i<mess.getPlayedByOthers().size(); i++){
                System.out.println(mess.getPlayedByOthers().get(i).toString());
            }
            for (int i = 0; i<mess.getRemainingAssistants().size(); i++) {
                switch (mess.getRemainingAssistants().get(i)){
                    case ONE:
                        if (mess.getPlayedByOthers().contains(Assistant.ONE)){
                            break;
                        }
                        one.setDisable(false); one.setVisible(true);
                        break;
                    case TWO:
                        if (mess.getPlayedByOthers().contains(Assistant.TWO)){
                            break;
                        }
                        two.setDisable(false); two.setVisible(true);
                        break;
                    case THREE:
                        if (mess.getPlayedByOthers().contains(Assistant.THREE)){
                            break;
                        }
                        three.setDisable(false); three.setVisible(true);
                        break;
                    case FOUR:
                        if (mess.getPlayedByOthers().contains(Assistant.FOUR)){
                            break;
                        }
                        four.setDisable(false); four.setVisible(true);

                    case FIVE:
                        if (mess.getPlayedByOthers().contains(Assistant.FIVE)){
                            break;
                        }
                        five.setDisable(false); five.setVisible(true);
                        break;
                    case SIX:
                        if (mess.getPlayedByOthers().contains(Assistant.SIX)){
                            break;
                        }
                        six.setDisable(false); six.setVisible(true);
                        break;
                    case SEVEN:
                        if (mess.getPlayedByOthers().contains(Assistant.SEVEN)){
                            break;
                        }
                        seven.setDisable(false); seven.setVisible(true);
                        break;
                    case EIGHT:
                        if (mess.getPlayedByOthers().contains(Assistant.EIGHT)){
                            break;
                        }
                        eight.setDisable(false); eight.setVisible(true);
                        break;
                    case NINE:
                        if (mess.getPlayedByOthers().contains(Assistant.NINE)){
                            break;
                        }
                        nine.setDisable(false); nine.setVisible(true);
                        break;
                    case TEN:
                        if (mess.getPlayedByOthers().contains(Assistant.TEN)){
                            break;
                        }
                        ten.setDisable(false); ten.setVisible(true);
                        break;
                }
            }
        }

    }
}
