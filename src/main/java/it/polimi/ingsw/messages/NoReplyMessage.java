package it.polimi.ingsw.messages;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.Alert.AlertType.WARNING;

public class NoReplyMessage extends Message {
    final String title;
    final String headerText;
    final String content;
    final boolean isWarning;

    public NoReplyMessage(boolean warning, String title,String headerText,String content) {
        this.isWarning = warning;
        this.title = title;
        this.headerText = headerText;
        this.content = content;
    }
    @Override
    public void switchAndFillView() {
        //make dialog showing content
        Platform.runLater(()->{
        Alert alert;
        if (isWarning){alert = new Alert(WARNING, content, ButtonType.OK);}
        else {alert = new Alert(INFORMATION,content,ButtonType.OK);}
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
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
