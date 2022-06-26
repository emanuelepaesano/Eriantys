package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PlayCharMessage;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.characters.Character;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.messages.PlayCharMessage.PlayCharType.play;
import static it.polimi.ingsw.messages.PlayCharMessage.PlayCharType.start;
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


    private Image yellow;private Image blue;private Image red;private Image green;private Image pink;


    Stage stage;
    NetworkHandler nh;
    Effect baseEffect;

    List<Button> buttons;

    List<ImageView> activeCharacters;
    List<List<AnchorPane>> studPanes;
    Map<Student, Image> fromStudToImage;


    public void initialize(){
        yellow = new Image("assets/student_yellow.png",true);
        blue =new Image("assets/student_blue.png",true);
        red =new Image("assets/student_red.png",true);
        green =new Image("assets/student_green.png",true);
        pink =new Image("assets/student_pink.png",true);

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
        bindStudents(charMessage.getCharacters());

    }

    private void bindStudents(List<Character> characters) {
        System.out.println("calling Bind Students");
        for (int i =0 ;i<3;i++) {
            if (characters.get(i).getStudents() == null){
                System.out.println("A null list with student");
                continue;}
            List<Student> characterStudents = characters.get(i).getStudents();
            List<AnchorPane> cardPanes = studPanes.get(i);
            if (characterStudents.size()>0){
                for (int j = 0; j<characterStudents.size();j++){
                    ImageView studentView = new ImageView(fromStudToImage.get(characterStudents.get(j)));
                    System.out.println("there should be an image "+ studentView);
                    studentView.setEffect(new DropShadow());
                    studentView.setVisible(true);
                    cardPanes.get(j).getChildren().setAll(studentView);
                    cardPanes.get(i).setVisible(true);
                }
            }

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
}
