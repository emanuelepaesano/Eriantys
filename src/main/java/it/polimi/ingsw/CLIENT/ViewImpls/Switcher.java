package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Switcher implements View {
    public AnchorPane container;
    public Button school;
    public Button islands;
    public SubScene gameScene;
    public Button assistants;

    private Stage stage;


    private UIManager uim;

    @Override
    public void display() {
        Parent root = UIManager.getUIManager().getSwitcherRoot();
        Scene sc;
        stage = UIManager.getUIManager().getMainWindow();
        if (root.getScene() == null) {
            sc = new Scene(root);
        } else sc = root.getScene();
        stage.setScene(sc);
        stage.setTitle("Eriantys");
        stage.sizeToScene();
        gameScene.setVisible(true);
        stage.setX(0);
        stage.setY(0);
        stage.show();
    }

    @Override
    public void sendReply() {

    }

    public void initialize(){
        uim = UIManager.getUIManager();
        uim.getGenInfoView();
        uim.getSchoolView();
        uim.getPlanningPhaseView();

    }
    @Override
    public void fillInfo(Message message) {

    }

    public void toIslands() {

        Platform.runLater(()->gameScene.setRoot(uim.getGenInfoRoot()));
    }

    public void toSchool() {

        Platform.runLater(()->gameScene.setRoot(uim.getSchoolRoot()));
    }

    public void toAssistants() {

        Platform.runLater(()->gameScene.setRoot(uim.getPlanningPhaseRoot()));
    }
}
