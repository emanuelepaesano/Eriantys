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
        Scene sc;
        stage = UIManager.getGuiManager().getMainWindow();
        if (root.getScene() == null) {
            sc = new Scene(root);
        }
        else sc = root.getScene();
        stage.setScene(sc);
        stage.setTitle("Login");
        stage.sizeToScene();
        stage.show();
    }

    public void initialize(){
        nh = UIManager.getGuiManager().getNh();
        nh.setMessageArrivedObserver((msg)-> {
            Send.setDisable(false);
                if (msg.getClass().getSimpleName().equals("StringMessage")) {
                    textArea.appendText("\n"+ msg);
                } else {
                    Platform.runLater(msg::switchAndFillView);
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