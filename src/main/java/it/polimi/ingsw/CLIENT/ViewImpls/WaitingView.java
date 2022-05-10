package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class WaitingView implements View{
    public ImageView imageView;
    Stage stage;



    @Override
    public void display() {
        stage = new Stage();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/WaitingView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene sc = new Scene(root);
        stage.setScene(sc);
        stage.setTitle("Waiting for Server");
        stage.sizeToScene();
        stage.show();
        System.out.println("fine display");
    }

    @Override
    public void display(Parent root) {

    }


    @Override
    public void sendReply() {}

    @Override
    public void fillInfo(Message message) {}


    public void close(){
        stage.close();
}
}
