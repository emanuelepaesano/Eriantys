package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.List;

public class NoReplyMessage extends Message {
    final String content;
    public NoReplyMessage(String content) {
        this.content = content;
    }


    @Override
    public void switchAndFillView() {
        //make dialog showing content
        Platform.runLater(()->{
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Disconnection");
        alert.setHeaderText("Player Disconnection");
        alert.setContentText(content);
        alert.setHeight(400);
        alert.setWidth(600);
        alert.showAndWait();
        });
    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public String toString() {
        return this.content;
    }

    public Boolean isRepliable() {
        return false;
    }

    public String getContent() {
        return content;
    }
}
