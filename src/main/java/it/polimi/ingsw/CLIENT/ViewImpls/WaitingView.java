package it.polimi.ingsw.CLIENT.ViewImpls;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WaitingView{
    public ImageView imageView;
    public TextField waitingText;
    public Text ipText;
    public TextField textInputField;
    public Button sendButton;
    public Text portText;
    Stage stage;
    Boolean what;
    String ip;

    Integer port;


    public void initialize(){
        what = false;
        textInputField.setFocusTraversable(false);
    }

    public void display(Parent root) {
        stage = new Stage();
        Scene sc = new Scene(root);
        stage.setScene(sc);
        stage.setTitle("Eriantys");
        stage.sizeToScene();
        stage.show();
        sc.setOnKeyPressed((e)->{if(e.getCode()== KeyCode.ENTER){
            write();}});
    }



    public void close(){
        stage.close();
}

    public void askPort() {
        what = true;
        ipText.setVisible(false);
        portText.setVisible(true);
        textInputField.setPromptText("1337 is the default port.");
    }

    public void write() {
        if (!what){
            if (textInputField.getText().equals("")) {
                this.ip = "localhost";
            }
            else {this.ip = textInputField.getText();}
            textInputField.clear();
        }
        else {
            if (textInputField.getText().equals("")) {this.port = 1337;}
            else {this.port = Integer.parseInt(textInputField.getText());}
            textInputField.clear();
        }
    }

    public void startWaiting(){
        textInputField.setVisible(false);
        portText.setVisible(false);
        sendButton.setVisible(false);
        waitingText.setVisible(true);
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }
}
