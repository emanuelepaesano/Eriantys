package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PlayCharMessage;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.characters.Character;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.messages.PlayCharMessage.PlayCharType.*;
import static it.polimi.ingsw.model.Student.*;


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
    public AnchorPane p11; public AnchorPane p12; public AnchorPane p13; public AnchorPane p14; public AnchorPane p15;public AnchorPane p16;
    public AnchorPane p21; public AnchorPane p22; public AnchorPane p23; public AnchorPane p24; public AnchorPane p25;public AnchorPane p26;
    public AnchorPane p31; public AnchorPane p32; public AnchorPane p33; public AnchorPane p34; public AnchorPane p35;public AnchorPane p36;
    public Text numCoins;
    public Text text1;public Text text2;public Text text3;
    public Text cost1;public Text cost2;public Text cost3;
    public Polyline fumetto1;public Polyline fumetto2;public Polyline fumetto3;
    public Button done1;public Button done2;public Button done3;


    Stage stage;
    NetworkHandler nh;
    Effect baseEffect;

    List<Button> buttons;
    List<ImageView> activeCharacters;
    List<List<AnchorPane>> studPanes;
    Map<Student, Image> fromStudToImage;
    Map<Student, EventHandler<MouseEvent>> fromStudToFunction;
    List<List<ImageView>> currentStudents;


    public void initialize(){
        Image yellow = new Image("assets/student_yellow.png",true);
        Image blue =new Image("assets/student_blue.png",true);
        Image red =new Image("assets/student_red.png",true);
        Image green =new Image("assets/student_green.png",true);
        Image pink =new Image("assets/student_pink.png",true);
        currentStudents = List.of(new ArrayList<>(List.of()),new ArrayList<>(List.of()),new ArrayList<>(List.of()));
        nh = UIManager.getUIManager().getNh();
        baseEffect = movetoDR.getEffect();
        studPanes = List.of(
                List.of(p11,p12,p13,p14,p15,p16),
                List.of(p21,p22,p23,p24,p25,p26),
                List.of(p31,p32,p33,p34,p35,p36)
        );
        fromStudToImage = new EnumMap<>(
                Map.of(YELLOW, yellow,
                        BLUE, blue,
                        RED, red,
                        PINK, pink,
                        GREEN, green)
        );
        fromStudToFunction = new EnumMap<>(
                Map.of(
                        YELLOW, this::sendYellow,
                        BLUE, this::sendBlue,
                        RED, this::sendRed,
                        PINK, this::sendPink,
                        GREEN, this::sendGreen
                )
        );
    }


    @Override
    public void display() {}

    @Override
    public void fillInfo(Message message) {
        PlayCharMessage charMessage = (PlayCharMessage) message;
        if (charMessage.getType().equals(chooseStudent)){
            int cardIndex = charMessage.getCharIndex();
            Platform.runLater(()->{
                bindOneCard(charMessage.getTempStudents(), cardIndex);
                enableDone(cardIndex);
                currentStudents.get(cardIndex).forEach(img->img.setDisable(false));
            });
            return;
        }

        if (charMessage.getType().equals(start)){
            activeCharacters = initializeCards(charMessage.getCharacters());
            activeCharacters.forEach(img->img.setDisable(true));
        }
        else if (charMessage.getType().equals(play)){
            activeCharacters.forEach(img->img.setDisable(false));
        }
        numCoins.setText(String.valueOf(charMessage.getPlayer().getCoins()));
        bindCost(charMessage.getCharacters());
        bindStudents(charMessage.getCharacters());
    }

    private void enableDone(int cardIndex) {
        switch (cardIndex){
            case 0 -> {if (activeCharacters.get(0).equals(exchangeStudents)){done1.setVisible(true);}}
            case 1 -> {if (activeCharacters.get(1).equals(exchangeStudents1)){done2.setVisible(true);}}
            case 2 -> {if (activeCharacters.get(2).equals(exchangeStudents3)){done3.setVisible(true);}}
            }
    }


    private void bindStudents(List<Character> characters) {
        for (int i =0 ;i<3;i++) {
            if (characters.get(i).getStudents() == null){
                continue;}
            List<Student> characterStudents = characters.get(i).getStudents();
            List<AnchorPane> cardPanes = studPanes.get(i);
            if (characterStudents.size()>0){
                bindOneCard(characterStudents,i);
            }
            currentStudents.forEach(card->card.forEach(img->img.setDisable(true)));
        }
    }


    private void bindOneCard(List<Student> students, int cardIndex) {
        List<AnchorPane> cardPanes = studPanes.get(cardIndex);
        cardPanes.forEach(pane->pane.getChildren().setAll());
        currentStudents.get(cardIndex).clear();
        for (int paneNum = 0; paneNum<students.size();paneNum++) {
            Student student = students.get(paneNum);
            ImageView studentView = new ImageView(fromStudToImage.get(student));
            currentStudents.get(cardIndex).add(studentView);
            studentView.setOnMouseEntered(this::enteredStudent);
            studentView.setOnMouseExited(this::exitedStudent);
            studentView.setOnMouseClicked(fromStudToFunction.get(student));
            studentView.setEffect(new DropShadow());
            studentView.setPickOnBounds(true);
            cardPanes.get(paneNum).getChildren().setAll(studentView);
        }
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


    public void sendYellow(MouseEvent mouseEvent){
        nh.sendReply("yellow");
    }
    public void sendBlue(MouseEvent mouseEvent){
        nh.sendReply("blue");
    }
    public void sendRed(MouseEvent mouseEvent){
        nh.sendReply("red");
    }
    public void sendPink(MouseEvent mouseEvent){
        nh.sendReply("pink");
    }
    public void sendGreen(MouseEvent mouseEvent){
        nh.sendReply("green");
    }
    public void sendBack(){
        nh.sendReply("back");
        done1.setVisible(false);
        done2.setVisible(false);
        done3.setVisible(false);
    }
    public void enteredStudent(MouseEvent event){
        ImageView student = (ImageView) event.getSource();
        Bloom effect = new Bloom();
        effect.setInput(new DropShadow());
        student.setEffect(effect);
    }

    public void exitedStudent(MouseEvent event){
        ImageView student = (ImageView) event.getSource();
        student.setEffect(new DropShadow());
    }

}
