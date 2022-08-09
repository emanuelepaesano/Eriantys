package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PlanningPhaseMessage;
import it.polimi.ingsw.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.Assistant.*;


public class PlanningPhaseView implements View {

    public ImageView one;public ImageView two;public ImageView three;public ImageView four;public ImageView five;
    public ImageView six;public ImageView seven;public ImageView eight;public ImageView nine;public ImageView ten;
    public ImageView one1;public ImageView two1;public ImageView three1;public ImageView four1;public ImageView five1;
    public ImageView six1;public ImageView seven1;public ImageView eight1;public ImageView nine1;public ImageView ten1;
    public ImageView one11;public ImageView two11;public ImageView three11;public ImageView four11;public ImageView five11;
    public ImageView six11;public ImageView seven11;public ImageView eight11;public ImageView nine11;public ImageView ten11;
    public ImageView one111;public ImageView two111;public ImageView three111;public ImageView four111;public ImageView five111;
    public ImageView six111;public ImageView seven111;public ImageView eight111;public ImageView nine111;public ImageView ten111;
    public Parent planningPhaseRoot;
    NetworkHandler nh;
    List<Assistant> allAssistants;
    private List<Assistant> remainingAssistants;
    private List<Assistant> playedByOthers;
    private double currentViewOrder;
    public Text p1name;
    public Text p2name;
    Map<ImageView,Double> viewOrders;
    List<ImageView> cardImages;
    List <ImageView> yourCard;
    List <ImageView> p1Cards;
    List <ImageView> p2Cards;


    public void initialize(){
        cardImages = List.of(one,two,three,four,five,six,seven,eight,nine,ten);
        nh = UIManager.getUIManager().getNh();
        allAssistants = List.of(ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,TEN);
        viewOrders = Map.of(one,10.0,two,9.0,three,8.0,four,7.0,five,6.0,
                six,5.0,seven,4.0,eight,3.0,nine,2.0,ten,1.0);
        yourCard = List.of(one1,two1,three1,four1,five1,six1,seven1,eight1,nine1,ten1);
        p1Cards = List.of(one11,two11,three11,four11,five11,six11,seven11,eight11,nine11,ten11);
        p2Cards = List.of(one111,two111,three111,four111,five111,six111,seven111,eight111,nine111,ten111);
        disableAllAssistants();
    }


    public Parent getPlanningPhaseRoot() {
        return planningPhaseRoot;
    }

    @Override
    public void display() {}

    @Override
    public void fillInfo(Message message) {
            PlanningPhaseMessage mess = (PlanningPhaseMessage) message;
            if (mess.getType() == PlanningPhaseMessage.PlanningPhaseType.ACTION){
                this.remainingAssistants = mess.getRemainingAssistants();
                this.playedByOthers = mess.getPlayedByOthers();
                Platform.runLater(()->{
                bindAssistants();
                setCardOpacity();
                });
                }
            else {
                yourCard.forEach(img->img.setVisible(false));
                Assistant current = mess.getCurrent();
                ImageView toEnable = yourCard.get(allAssistants.indexOf(current));
                toEnable.setVisible(true);
            }
    }

    private void bindAssistants(){
        for (int i=0; i<10;i++){
            Assistant assistant = allAssistants.get(i);
            if (remainingAssistants.contains(assistant)){
                if (!playedByOthers.contains(assistant)||playedByOthers.equals(remainingAssistants)){
                    cardImages.get(i).setDisable(false);
                }
            }
        }
    }
    private void setCardOpacity(){
        for (ImageView img :cardImages){
            if (img.isDisable()){
                img.setOpacity(0.5);
                img.setViewOrder(100.0);
            }
            else {img.setOpacity(1);}
        }
    }
    public void disableAllAssistants(){
        one.setDisable(true);
        two.setDisable(true);
        three.setDisable(true);
        four.setDisable(true);
        five.setDisable(true);
        six.setDisable(true);
        seven.setDisable(true);
        eight.setDisable(true);
        nine.setDisable(true);
        ten.setDisable(true);
        setCardOpacity();
    }


    public void enterCard(MouseEvent actionEvent) {
        ImageView cardToHighlight = (ImageView) actionEvent.getSource();
        currentViewOrder = viewOrders.get(cardToHighlight);
        cardToHighlight.setViewOrder(-1.0);
    }
    public void exitCard(MouseEvent actionEvent) {
        ImageView cardToHighlight = (ImageView) actionEvent.getSource();
        cardToHighlight.setViewOrder(currentViewOrder);
    }
    public void send1(MouseEvent actionEvent) {
        nh.sendReply(("one"));
        disableAllAssistants();
    }
    public void send2(MouseEvent actionEvent) {
        nh.sendReply(("two"));
        disableAllAssistants();
    }
    public void send3(MouseEvent actionEvent) {
        nh.sendReply(("three"));
        disableAllAssistants();
    }
    public void send4(MouseEvent actionEvent) {
        nh.sendReply(("four"));
        disableAllAssistants();
    }
    public void send5(MouseEvent actionEvent) {
        nh.sendReply(("five"));
        disableAllAssistants();
    }
    public void send6(MouseEvent actionEvent) {
        nh.sendReply(("six"));
        disableAllAssistants();
    }
    public void send7(MouseEvent actionEvent) {
        nh.sendReply(("seven"));
        disableAllAssistants();
    }
    public void send8(MouseEvent actionEvent) {
        nh.sendReply(("eight"));
        disableAllAssistants();
    }
    public void send9(MouseEvent actionEvent) {
        nh.sendReply(("nine"));
        disableAllAssistants();
    }
    public void send10(MouseEvent actionEvent) {
        nh.sendReply(("ten"));
        disableAllAssistants();
    }
}
