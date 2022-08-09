package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.SwitcherMessage;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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

    List<Player> players;

    private UIManager uim;

    @Override
    public void display() {
        Parent root = UIManager.getUIManager().getSwitcherRoot();
        Scene sc;
        Stage stage = UIManager.getUIManager().getMainWindow();
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

    public void initialize(){
        uim = UIManager.getUIManager();
        uim.getIslandView();
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
        if (!message.isGameAdvanced()){
            characters.setVisible(false);
        }
        if (this.players.size() == 1) {
            player1.setText(this.players.get(0).getPlayerName());
            player1.setVisible(true);
            player2.setVisible(false);
            OtherPlayerView p1View = (OtherPlayerView) uim.getP1SchoolView();
            p1View.fillInfoWithPlayer(this.players.get(0));
            PlanningPhaseView assistantView = (PlanningPhaseView) uim.getPlanningPhaseView();
            assistantView.p1name.setText(this.players.get(0).getPlayerName()+":");
            bindP1(assistantView);
        } else {
            player1.setText(this.players.get(0).getPlayerName());
            player2.setText(this.players.get(1).getPlayerName());
            player1.setVisible(true);
            player2.setVisible(true);
            OtherPlayerView p1View = (OtherPlayerView) uim.getP1SchoolView();
            OtherPlayerView p2View = (OtherPlayerView) uim.getP2SchoolView();
            PlanningPhaseView assistantView = (PlanningPhaseView) uim.getPlanningPhaseView();
            assistantView.p1name.setText(this.players.get(0).getPlayerName()+":");
            assistantView.p2name.setVisible(true);
            assistantView.p2name.setText(this.players.get(1).getPlayerName()+":");
            p1View.fillInfoWithPlayer(this.players.get(0));
            bindP1(assistantView);
            p2View.fillInfoWithPlayer(this.players.get(1));
            bindP2(assistantView);
        }
    }

    public void bindP1(PlanningPhaseView assistantView) {
        assistantView.p1Cards.forEach(img->img.setVisible(false));
        Player p1 = this.players.get(0);
        if (p1.getCurrentAssistant() != null){
            ImageView toEnable = assistantView.p1Cards.get(assistantView.allAssistants.indexOf(p1.getCurrentAssistant()));
            toEnable.setVisible(true);
        }
    }

    public void bindP2(PlanningPhaseView assistantView) {
        assistantView.p2Cards.forEach(img->img.setVisible(false));
        Player p2 = this.players.get(0);
        if (p2.getCurrentAssistant() != null){
            ImageView toEnable = assistantView.p1Cards.get(assistantView.allAssistants.indexOf(p2.getCurrentAssistant()));
            toEnable.setVisible(true);
        }
    }

    public void toIslands() {
        Platform.runLater(()->gameScene.getChildren().setAll(uim.getIslandRoot()));
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
