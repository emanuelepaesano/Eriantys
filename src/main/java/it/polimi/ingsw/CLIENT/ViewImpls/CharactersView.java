package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PlayCharMessage;
import it.polimi.ingsw.model.characters.Character;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.messages.PlayCharMessage.PlayCharType.play;
import static it.polimi.ingsw.messages.PlayCharMessage.PlayCharType.start;


public class CharactersView implements View {


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
    public Text cost1;public Text cost2;public Text cost3;
    public Polyline fumetto1;public Polyline fumetto2;public Polyline fumetto3;
    public Parent charactersRoot;

    Stage stage;
    NetworkHandler nh;
    Effect baseEffect;

    List<Button> buttons;

    List<ImageView> activeCharacters;

    public void initialize(){
        nh = UIManager.getUIManager().getNh();
        baseEffect = movetoDR.getEffect();

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
        PlayCharMessage charMessage = (PlayCharMessage) message;
        if (charMessage.getType().equals(start)){
            activeCharacters = initializeCards(charMessage.getCharacters());
            activeCharacters.forEach(img->img.setDisable(true));
        }
        else if (charMessage.getType().equals(play)){
            activeCharacters.forEach(img->img.setDisable(false));
        }
        numCoins.setText(String.valueOf(charMessage.getPlayer().getCoins()));
        bindCost(charMessage.getCharacters());

    }

    private void bindCost(List<Character> characters) {
        List<Text> costs = List.of(cost1,cost2,cost3);
        for (int i = 0; i<3;i++){
            Character character = characters.get(i);
            costs.get(i).setText(String.valueOf(character.getCost()));
        }
    }

    private List<ImageView> initializeCards(List<Character> messChars) {
        List<ImageView> actives = new ArrayList<>();
        List<Text> fumetti = List.of(text1,text2,text3);
        List<List<ImageView>> allCharacters =
                List.of(
                    List.of(checkOwner,orEqual,moreInfluence,moreMovements,movetoDR,noTowers,
                            placeInIsland,zeroPointStudent,exchangeStudents, moveEntranceDR,returnStudent,
                             blockIsland),
                    List.of(checkOwner1,orEqual1,moreInfluence1,moreMovements1,movetoDR1,noTowers1,
                            placeInIsland1,zeroPointStudent1,exchangeStudents1, moveEntranceDR1,returnStudent1,
                            blockIsland1),
                    List.of(checkOwner3,orEqual3,moreInfluence3,moreMovements3,movetoDR3,noTowers3,
                            placeInIsland3,zeroPointStudent3,exchangeStudents3, moveEntranceDR3,returnStudent3,
                            blockIsland3)
                );
        for (int i =0;i<3;i++){
            Character character = messChars.get(i);
            fumetti.get(i).setText(character.getDescription());
            int charIndex = character.getNumber()-1;
            ImageView active = allCharacters.get(i).get(charIndex);
            active.setVisible(true);
            actives.add(active);
        }
        return actives;
    }


    public void disableAllCharacters(){
        activeCharacters.forEach(img->img.setDisable(true));
    }

    public void send1(MouseEvent actionEvent) {
        nh.sendReply(("1"));
        disableAllCharacters();
    }
    public void send2(MouseEvent actionEvent) {
        nh.sendReply(("2"));
        disableAllCharacters();
    }
    public void send3(MouseEvent actionEvent) {
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
