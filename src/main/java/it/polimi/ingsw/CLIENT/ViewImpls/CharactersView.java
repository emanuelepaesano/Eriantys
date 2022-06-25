package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.characters.Character;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;


public class CharactersView implements View {

    public Button one;
    public Button two;
    public Button three;

    public ImageView checkOwner; public ImageView moreInfluence; public ImageView noTowers;
    public ImageView orEqual; public ImageView zeroPointStudent; public ImageView exchangeStudents;
    public ImageView returnStudent; public ImageView moveEntranceDR; public ImageView placeInIsland;
    public ImageView blockIsland; public ImageView moreMovements; public ImageView movetoDR;

    public ImageView checkOwner1; public ImageView moreInfluence1; public ImageView noTowers1;
    public ImageView orEqual1; public ImageView zeroPointStudent1; public ImageView exchangeStudents1;
    public ImageView returnStudent1; public ImageView moveEntranceDR1; public ImageView placeInIsland1;
    public ImageView blockIsland1; public ImageView moreMovements1; public ImageView movetoDR1;

    public ImageView checkOwner3; public ImageView moreInfluence3; public ImageView noTowers3;
    public ImageView orEqual3; public ImageView zeroPointStudent3; public ImageView exchangeStudents3;
    public ImageView returnStudent3; public ImageView moveEntranceDR3; public ImageView placeInIsland3;
    public ImageView blockIsland3; public ImageView moreMovements3; public ImageView movetoDR3;

    public Text numCoins;
    public Text text1;public Text text2;public Text text3;
    public Polyline fumetto1;public Polyline fumetto2;public Polyline fumetto3;
    public Parent charactersRoot;

    Stage stage;
    NetworkHandler nh;
    Effect baseEffect;

    List<Character> allCharacters;
    List<Button> buttons;

    public void initialize(){
        disableAllCharacters();
        nh = UIManager.getUIManager().getNh();
        allCharacters = null;
        buttons = List.of(one,two,three);
        baseEffect = movetoDR.getEffect();
    }


    public Parent getCharactersRoot() {
        return charactersRoot;
    }

    @Override
    public void display() {
        Parent root = UIManager.getUIManager().getCharactersRoot();
        stage = UIManager.getUIManager().getMainWindow();
        Scene sc;
        if (root.getScene() == null) {
            sc = new Scene(root);
        }
        else sc = root.getScene();
        stage.setScene(sc);
        stage.setTitle("Characters");
        stage.sizeToScene();
        stage.show();
    }

    @Override
    public void sendReply() {

    }

//    todo: update the cost of characters
    @Override
    public void fillInfo(Message message) {

    }

//
    private void bindCharacters(){
//        for (int i=0; i<10;i++){
//            Assistant assistant = allCharacters.get(i);
//            if (remainingCharacters.contains(assistant)){
//                if (!playedByOthers.contains(assistant)||playedByOthers.equals(remainingCharacters)){
//                    buttons.get(i).setDisable(false);
//                }
//            }
//        }
    }

    public void disableAllCharacters(){
        one.setDisable(true);
        two.setDisable(true);
        three.setDisable(true);
    }

    public void send1(ActionEvent actionEvent) {
        nh.sendReply(("1"));
        disableAllCharacters();
    }
    public void send2(ActionEvent actionEvent) {
        nh.sendReply(("2"));
        disableAllCharacters();
    }
    public void send3(ActionEvent actionEvent) {
        nh.sendReply(("3"));
        disableAllCharacters();
    }

    public void enterCard1(MouseEvent mouseEvent) {
       ImageView card = (ImageView) mouseEvent.getSource();
       DropShadow dropShadow = new DropShadow();
       card.setEffect(dropShadow);
       fumetto1.setVisible(true);
       text1.setVisible(true);
    }

    public void enterCard2(MouseEvent mouseEvent) {
        ImageView card = (ImageView) mouseEvent.getSource();
        DropShadow dropShadow = new DropShadow();
        card.setEffect(dropShadow);
        fumetto2.setVisible(true);
        text2.setVisible(true);
    }

    public void enterCard3(MouseEvent mouseEvent) {
        ImageView card = (ImageView) mouseEvent.getSource();
        DropShadow dropShadow = new DropShadow();
        card.setEffect(dropShadow);
        fumetto3.setVisible(true);
        text3.setVisible(true);
    }

    public void exitCard(MouseEvent mouseEvent){
        ImageView card = (ImageView) mouseEvent.getSource();
        card.setEffect(baseEffect);
        text1.setVisible(false);
        fumetto1.setVisible(false);
        text2.setVisible(false);
        fumetto2.setVisible(false);
        text3.setVisible(false);
        fumetto3.setVisible(false);
    }
}
