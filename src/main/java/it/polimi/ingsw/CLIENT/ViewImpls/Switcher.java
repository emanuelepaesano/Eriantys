package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PlanningPhaseMessage;
import javafx.scene.DepthTest;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Switcher implements View {
    public AnchorPane container;
    public GridPane islandTab;
    public AnchorPane schoolTab;
    public GridPane assistantTab;
    public Button school;
    public Button islands;
    public SubScene gameScene;
    public Button assistants;

    private Stage stage;
    private View schoolView;
    private View planningPhaseView;
    private View genInfoView;

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
        stage.show();
    }

    @Override
    public void sendReply() {

    }

    public void initialize(){
        uim = UIManager.getUIManager();
        schoolView = uim.getActionPhaseView();
        planningPhaseView = uim.getPlanningPhaseView();
        genInfoView = uim.getGenInfoView();
    }
    @Override
    public void fillInfo(Message message) {

    }

    public void toIslands() {
        gameScene.setRoot(uim.getGenInfoRoot());
        gameScene.setVisible(true);

    }

    public void toSchool() {
        gameScene.setRoot(uim.getActionPhaseRoot());
    }

    public void toAssistants() {
        gameScene.setRoot(uim.getPlanningPhaseRoot());
    }
}
