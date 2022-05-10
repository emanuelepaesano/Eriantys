package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LoginView implements View {
    public AnchorPane logPane;
    String content;

    String reply;
    Scanner scanner = new Scanner(System.in);
    NetworkHandler nh;

    public Button Send;
    public TextField textField;
    public TextArea textArea;

    Stage stage;


    //accessed only by the user view with update()

    @Override
    public void display() {

    }

    @Override
    public void display(Parent root) {
        stage = UIManager.getMainWindow();
        Scene sc = new Scene(root);
        stage.setScene(sc);
        stage.setTitle("Login");
        stage.sizeToScene();
        stage.show();
    }

    public void initialize(){
        nh = UIManager.getNh();
        nh.setMessageArrivedObserver((msg)-> {
            Send.setDisable(false);
                if (msg.getView().equals("simpleview")) {
                    textArea.appendText("\n"+ msg);
                } else {
                    Platform.runLater(()->UIManager.getGuiManager().selectAndFillView(msg));
                }
            }
        );
    }

    @Override
    public void sendReply() {
    }


    @Override
    public void fillInfo(Message message) {
        //fill buttons/other component with content field info.
       textArea.setText(message.toString());
    }


    public void doSomething(ActionEvent actionEvent) {
        textArea.appendText("   <Message sent to server.>");
        Send.setDisable(true);
        nh.sendMessage(new StringMessage(textField.getText()));
    }


}