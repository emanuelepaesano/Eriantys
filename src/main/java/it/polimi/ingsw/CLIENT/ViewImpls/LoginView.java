package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class LoginView implements View {
    String content;

    String reply;
    Scanner scanner = new Scanner(System.in);
    NetworkHandler nh;

    public Button Send;
    public TextField textField;
    public TextArea textArea;

    Stage stage;


    public LoginView() {
    }

    //accessed only by the user view with update()

    @Override
    public void display() {
        stage.show();
    }


    @Override
    public void sendReply() {
    }


    @Override
    public void fillInfo(Message message) {
        //fill buttons/other component with content field info.
        stage = UIManager.getMainWindow();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene sc = new Scene(root);
        stage.setScene(sc);
        stage.setTitle("Login");
        stage.sizeToScene();
        textArea.setText(message.toString());
    }

    public void doSomething(ActionEvent actionEvent) {
        nh.sendMessage(new StringMessage(textField.getText()));
    }
}