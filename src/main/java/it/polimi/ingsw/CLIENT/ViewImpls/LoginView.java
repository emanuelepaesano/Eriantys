package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.GUIManager;
import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.LoginMessage;
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

import javax.swing.*;
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

    //static buttons and stuff


    public LoginView() {

    }

    //accessed only by the user view with update()

    @Override
    public void display() {
        Stage stage = GUIManager.getMainWindow();
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
        nh.sendMessage(new StringMessage(textField.getText()));
    }
}