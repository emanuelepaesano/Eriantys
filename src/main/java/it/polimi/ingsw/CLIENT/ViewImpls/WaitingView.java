package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class WaitingView implements View {

    @Override
    public void display() {
//        try {
//            root = FXMLLoader.load(getClass().getResource("/WaitingView.fxml"));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//        Scene sc = new Scene(root);
//        primaryStage.setScene(sc);
//        primaryStage.setTitle("Waiting for Server");
//        primaryStage.sizeToScene();
//primaryScene.setScene(this)
    }

    @Override
    public void sendReply() {}

    @Override
    public void fillInfo(Message message) {
    }
}
