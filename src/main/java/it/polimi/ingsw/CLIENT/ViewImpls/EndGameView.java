package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.EndGameMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class EndGameView implements View {

    public Text endText;
    public AnchorPane endRoot;
    Stage stage;

    NetworkHandler nh;
    @Override
    public void display() {
        Parent root = UIManager.getUIManager().getEndGameRoot();
        Scene sc;
        stage = UIManager.getUIManager().getMainWindow();
        if (root.getScene() == null) {
            sc = new Scene(root);
        }
        else sc = root.getScene();
        Platform.runLater(()-> {
            stage.setScene(sc);
            stage.setTitle("Game Over");
            stage.sizeToScene();
            stage.show();
        });
    }


    public void initialize(){
        nh = UIManager.getUIManager().getNh();
        Image img = new Image("assets/pastel blue background.jpg");
        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        endRoot.setBackground(bGround);
    }
    @Override
    public void fillInfo(Message message) {
        EndGameMessage endGameMessage = (EndGameMessage) message;
        switch(endGameMessage.getType()){
            case TIE -> {showTieScreen(endGameMessage.getOtherWinners());}
            case WIN -> {showWinScreen();}
            case LOSE -> {showLoseScreen(endGameMessage.getOtherWinners());}
        }
        nh.endConnectionandGame();
    }

    private void showLoseScreen(List<Player> otherWinners) {
        String text = "You lose ... Better luck next time :) \n";
        for (Player player : otherWinners){
            text += player.toString() + " ";
        }
        text += "won the game.";
        endText.setText(text);
    }

    private void showWinScreen() {
        endText.setText("You won the game!!! \nCongratulations!");

    }

    private void showTieScreen(List<Player> otherWinners) {
        String text = "You tied! You won together with \n";
        for (Player player : otherWinners){
            text += player.toString() + " ";
        }
        endText.setText(text);
    }

    public void closeGame(){
        Platform.exit();
        System.exit(0);
    }
}
