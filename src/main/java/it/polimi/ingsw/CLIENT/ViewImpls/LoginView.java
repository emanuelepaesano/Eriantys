package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.LoginMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.NoReplyMessage;
import it.polimi.ingsw.model.TowerColor;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Scanner;

public class LoginView implements View {

    public AnchorPane logPane;
    public ImageView blackSmall;
    public ImageView blackBig;
    public ImageView whiteSmall;
    public ImageView whiteBig;
    public ImageView greySmall;
    public ImageView greyBig;
    public Label enterNick;
    public ImageView eriantysLogo;
    public ImageView pastelBack;
    public Button Send;
    public TextField textField;
    public Label chooseTower;
    public ImageView oneSmall;
    public ImageView twoSmall;
    public ImageView threeSmall;
    public ImageView fourSmall;
    public ImageView oneBig;
    public ImageView twoBig;
    public ImageView threeBig;
    public ImageView fourBig;
    public Label chooseWiz;
    private List<Node> allNodes;


    private List<TowerColor> remainingColors;
    private List<Integer> remainingWiz;
    String content;

    String reply;
    Scanner scanner = new Scanner(System.in);
    NetworkHandler nh;


    Stage stage;


    @Override
    public void display() {
        Parent root = UIManager.getUIManager().getLoginRoot();
        Scene sc;
        stage = UIManager.getUIManager().getMainWindow();
        if (root.getScene() == null) {
            sc = new Scene(root);
        }
        else sc = root.getScene();
        stage.setScene(sc);
        stage.setTitle("Login");
        stage.sizeToScene();
        stage.show();
        sc.setOnKeyPressed((e)->{if(e.getCode()== KeyCode.ENTER){
            sendName();}});
    }



    public void initialize(){
        nh = UIManager.getUIManager().getNh();
        allNodes = List.of(pastelBack,blackSmall,blackBig,whiteSmall,whiteBig,
                greySmall,greyBig,enterNick,eriantysLogo,Send,textField,chooseTower,
                oneSmall,oneBig,twoSmall,twoBig,threeSmall,threeBig,fourSmall,fourBig,chooseTower);

        nh.setMessageArrivedObserver((msg)-> {
                allNodes.forEach(n->n.setDisable(false));
                if (msg.getClass().getSimpleName().equals("NoReplyMessage")) {
                    //show a dialog with error
                    Platform.runLater(()->{
                    NoReplyMessage noRep = (NoReplyMessage) msg;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Name");
                    alert.setContentText(noRep.getContent());
                    alert.initModality(Modality.NONE);
                    alert.showAndWait();
                    });
                }
                else if (msg.getClass().getSimpleName().equals("LoginMessage")) {
                    LoginMessage lmsg = (LoginMessage) msg;
                    if (lmsg.getType().equals("tower")){
//                        System.out.println("going to sleep");
//                        try{Thread.sleep(10000);}catch (Exception ex){}
                        allNodes.forEach(node -> node.setVisible(false));
                        remainingColors = lmsg.getAvailableColors();
                        Image img = new Image("assets/pastel blue background.jpg");
                        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                        Background bGround = new Background(bImg);
                        logPane.setBackground(bGround);
                        chooseTower.setVisible(true);
                        if (remainingColors.contains(TowerColor.WHITE)){whiteSmall.setVisible(true);}
                        if (remainingColors.contains(TowerColor.BLACK)){blackSmall.setVisible(true);}
                        if (remainingColors.contains(TowerColor.GREY)){greySmall.setVisible(true);}
                    }
                    else if (lmsg.getType().equals("wizard")){
                        allNodes.forEach(node -> node.setVisible(false));
                        remainingWiz = lmsg.getAvailableWiz();
                        List<ImageView> wizards = List.of(oneSmall,twoSmall,threeSmall,fourSmall);
                        wizards.forEach(w->{w.setVisible(true);w.setDisable(true);w.setOpacity(0.7);});
                        chooseWiz.setVisible(true);
                        if (remainingWiz.contains(1)){oneSmall.setDisable(false);oneSmall.setOpacity(1);oneBig.setOpacity(1);}
                        if (remainingWiz.contains(2)){twoSmall.setDisable(false);twoSmall.setOpacity(1);twoBig.setOpacity(1);}
                        if (remainingWiz.contains(3)){threeSmall.setDisable(false);threeSmall.setOpacity(1);threeBig.setOpacity(1);}
                        if (remainingWiz.contains(4)){fourSmall.setDisable(false);fourSmall.setOpacity(1);fourBig.setOpacity(1);}
                    }
                }
                else Platform.runLater(msg::switchAndFillView);
            }
        );
    }

    @Override
    public void sendReply() {
    }


    @Override
    public void fillInfo(Message message) {
        //fill buttons/other component with content field info.
        LoginMessage lmsg = (LoginMessage) message;
        remainingColors = lmsg.getAvailableColors();
        remainingWiz = lmsg.getAvailableWiz();
    }


    public void sendName() {
        Send.setDisable(true);
        nh.sendReply((textField.getText()));
        textField.clear();
    }


    public void mouseEnteredWhite(){
        whiteSmall.setVisible(false);
        whiteBig.setVisible(true);
    }
    public void mouseExitedWhite(){
        whiteSmall.setVisible(true);
        whiteBig.setVisible(false);
    }
    public void mouseEnteredBlack(){
        blackSmall.setVisible(false);
        blackBig.setVisible(true);
    }
    public void mouseExitedBlack(){
        blackSmall.setVisible(true);
        blackBig.setVisible(false);
    }
    public void mouseEnteredGrey(){
        greySmall.setVisible(false);
        greyBig.setVisible(true);
    }
    public void mouseExitedGrey(){
        greySmall.setVisible(true);
        greyBig.setVisible(false);
    }

    public void sendWhite(MouseEvent mouseEvent) {
        allNodes.forEach(node -> node.setDisable(true));
        nh.sendReply("white");
    }
    public void sendGrey(MouseEvent mouseEvent) {
        allNodes.forEach(node -> node.setDisable(true));
        nh.sendReply("grey");
    }
    public void sendBlack(MouseEvent mouseEvent) {
        allNodes.forEach(node -> node.setDisable(true));
        nh.sendReply("black");
    }

    public void mouseEnteredOne(){
        oneSmall.setVisible(false);
        oneBig.setVisible(true);
    }
    public void mouseExitedOne(){
        oneSmall.setVisible(true);
        oneBig.setVisible(false);
    }
    public void mouseEnteredTwo(){
        twoSmall.setVisible(false);
        twoBig.setVisible(true);
    }
    public void mouseExitedTwo(){
        twoSmall.setVisible(true);
        twoBig.setVisible(false);
    }
    public void mouseEnteredThree(){
        threeSmall.setVisible(false);
        threeBig.setVisible(true);
    }
    public void mouseExitedThree(){
        threeSmall.setVisible(true);
        threeBig.setVisible(false);
    }
    public void mouseEnteredFour(){
        fourSmall.setVisible(false);
        fourBig.setVisible(true);
    }
    public void mouseExitedFour(){
        fourSmall.setVisible(true);
        fourBig.setVisible(false);
    }

    public void send1(){
        allNodes.forEach(n->n.setDisable(true));
        nh.sendReply("1");
    }
    public void send2(){
        allNodes.forEach(n->n.setDisable(true));
        nh.sendReply("2");
    }
    public void send3(){
        allNodes.forEach(n->n.setDisable(true));
        nh.sendReply("3");
    }
    public void send4(){
        allNodes.forEach(n->n.setDisable(true));
        nh.sendReply("4");
    }

}