package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.messages.ActionPhaseMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.SwitcherMessage;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Switcher implements View {

    public AnchorPane mainPane;
    public AnchorPane container;
    public Button school;
    public Button islands;
    public StackPane gameScene;
    public Button assistants;
    public Button player1;
    public Button player2;
    public Button characters;

    private Stage stage;

    List<Player> players;

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
        uim.getCharactersView();
        Image img = new Image("assets/pastel blue background.jpg");
        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        mainPane.setBackground(bGround);
    }

    @Override
    public void fillInfo(Message mes) {
        SwitcherMessage message = (SwitcherMessage) mes;
        this.players = message.getOtherPlayers();
        if (this.players.size() == 1) {
            player1.setText(this.players.get(0).getPlayerName());
            player1.setVisible(true);
            player2.setVisible(false);
            OtherPlayerView p1View = (OtherPlayerView) uim.getP1SchoolView();
            p1View.fillInfoWithPlayer(this.players.get(0));
        } else {
            player1.setText(this.players.get(0).getPlayerName());
            player2.setText(this.players.get(1).getPlayerName());
            player1.setVisible(true);
            player2.setVisible(true);
            OtherPlayerView p1View = (OtherPlayerView) uim.getP1SchoolView();
            OtherPlayerView p2View = (OtherPlayerView) uim.getP2SchoolView();
            p1View.fillInfoWithPlayer(this.players.get(0));
            p2View.fillInfoWithPlayer(this.players.get(1));
        }
    }

    public void toIslands() {
        Platform.runLater(()->gameScene.getChildren().setAll(uim.getGenInfoRoot()));
    }

    public void toSchool() {
        Platform.runLater(()->gameScene.getChildren().setAll(uim.getSchoolRoot()));
    }

    public void toAssistants() {
        Platform.runLater(()->gameScene.getChildren().setAll(uim.getPlanningPhaseRoot()));
    }

    public void toCharacters() {
        Platform.runLater(()->gameScene.getChildren().setAll(uim.getCharactersRoot()));
    }

    public void toPlayer1() {
        Platform.runLater(()->gameScene.getChildren().setAll(uim.getP1SchoolRoot()));
    }

    public void toPlayer2() {
        Platform.runLater(()->gameScene.getChildren().setAll(uim.getP2SchoolRoot()));
    }

}
