package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.IslandMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.*;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.messages.IslandMessage.IslandMessageType.*;

public class IslandView implements View {

    public Label red0; public Label yellow0; public Label pink0; public Label green0; public Label blue0;
    public Label red1; public Label yellow1; public Label pink1; public Label green1; public Label blue1;
    public Label red2; public Label yellow2; public Label pink2; public Label green2; public Label blue2;
    public Label red3; public Label yellow3; public Label pink3; public Label green3; public Label blue3;
    public Label red4; public Label yellow4; public Label pink4; public Label green4; public Label blue4;
    public Label red5; public Label yellow5; public Label pink5; public Label green5; public Label blue5;
    public Label red6; public Label yellow6; public Label pink6; public Label green6; public Label blue6;
    public Label red7; public Label yellow7; public Label pink7; public Label green7; public Label blue7;
    public Label red8; public Label yellow8; public Label pink8; public Label green8; public Label blue8;
    public Label red9; public Label yellow9; public Label pink9; public Label green9; public Label blue9;
    public Label red10; public Label yellow10; public Label pink10; public Label green10; public Label blue10;
    public Label red11; public Label yellow11; public Label pink11; public Label green11; public Label blue11;

    public ImageView c1y1; public ImageView c1y2; public ImageView c1y3; public ImageView c1y4;
    public ImageView c2y1; public ImageView c2y2; public ImageView c2y3; public ImageView c2y4;
    public ImageView c3y1; public ImageView c3y2; public ImageView c3y3; public ImageView c3y4;
    public ImageView c1r1; public ImageView c1r2; public ImageView c1r3; public ImageView c1r4;
    public ImageView c2r1; public ImageView c2r2; public ImageView c2r3; public ImageView c2r4;
    public ImageView c3r1; public ImageView c3r2; public ImageView c3r3; public ImageView c3r4;
    public ImageView c1p1; public ImageView c1p2; public ImageView c1p3; public ImageView c1p4;
    public ImageView c2p1; public ImageView c2p2; public ImageView c2p3; public ImageView c2p4;
    public ImageView c3p1; public ImageView c3p2; public ImageView c3p3; public ImageView c3p4;
    public ImageView c1g1; public ImageView c1g2; public ImageView c1g3; public ImageView c1g4;
    public ImageView c2g1; public ImageView c2g2; public ImageView c2g3; public ImageView c2g4;
    public ImageView c3g1; public ImageView c3g2; public ImageView c3g3; public ImageView c3g4;
    public ImageView c1b1; public ImageView c1b2; public ImageView c1b3; public ImageView c1b4;
    public ImageView c2b1; public ImageView c2b2; public ImageView c2b3; public ImageView c2b4;
    public ImageView c3b1; public ImageView c3b2; public ImageView c3b3; public ImageView c3b4;

    public ImageView mn1;public ImageView mn2;public ImageView mn3;public ImageView mn4;
    public ImageView mn5;public ImageView mn6;public ImageView mn7;public ImageView mn8;
    public ImageView mn9;public ImageView mn10;public ImageView mn11;public ImageView mn0;

    public ImageView b01;public ImageView b12;public ImageView b23;public ImageView b34;
    public ImageView b45;public ImageView b56;public ImageView b67;public ImageView b78;
    public ImageView b89;public ImageView b910;public ImageView b1011;public ImageView b110;

    public ImageView tow_0b;public ImageView tow_0w;public ImageView tow_0g;public ImageView tow_1b;
    public ImageView tow_1w;public ImageView tow_1g;public ImageView tow_2b;public ImageView tow_2w;
    public ImageView tow_2g;public ImageView tow_3b;public ImageView tow_3w;public ImageView tow_3g;
    public ImageView tow_4b;public ImageView tow_4w;public ImageView tow_4g;public ImageView tow_5b;
    public ImageView tow_5w;public ImageView tow_5g;public ImageView tow_6b;public ImageView tow_6w;
    public ImageView tow_6g;public ImageView tow_7b;public ImageView tow_7w;public ImageView tow_7g;
    public ImageView tow_8b;public ImageView tow_8w;public ImageView tow_8g;public ImageView tow_9b;
    public ImageView tow_9w;public ImageView tow_9g;public ImageView tow_10w;public ImageView tow_10g;
    public ImageView tow_11b;public ImageView tow_11w;public ImageView tow_11g;public ImageView tow_10b;

    public ImageView island0; public ImageView island1; public ImageView island2; public ImageView island3;
    public ImageView island4; public ImageView island5; public ImageView island6; public ImageView island7;
    public ImageView island8; public ImageView island9; public ImageView island10; public ImageView island11;
    public ImageView c3;


    private List<ImageView> islands;
    private int numClouds;
    GameMap map;
    List<List<Student>> clouds;
    List<Player> players;
    Stage stage;
    NetworkHandler nh;
    int numPlayers;

    List<List<List<ImageView>>> cloudStuds;

    @Override
    public void display() {
        Parent root = UIManager.getUIManager().getGenInfoRoot();
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
        nh = UIManager.getUIManager().getNh();
        nh.setMessageArrivedObserver(Message::switchAndFillView);
        islands = List.of(island0,island1,island2,island3, island4, island5,
                island6,island7, island8,island9,island10,island11);
        islands.forEach(i->i.setDisable(true));
    }

    @Override
    public void fillInfo(Message mes) {
        IslandMessage message = (IslandMessage) mes;
        if (message.getType().equals(init)){
            numPlayers = message.getNumPlayers();
            clouds = message.getClouds();
            numClouds = clouds.size();
            initCloudStuds();
            map = message.getMap();
            players = message.getPlayers();
            bindAllLabels();
            bindMotherNature();
            bindClouds();
            addBridges();
            showTowers();
        }
        else if (message.getType().equals(updateMap)) {
            map = message.getMap();
            players = message.getPlayers();
            clouds = message.getClouds();

            //Here we have to link the elements from the model to the graphic components.
            bindAllLabels();
            bindMotherNature();
            bindClouds();
            addBridges();
            showTowers();
        }
        else if (message.getType().equals(moveMN)){
            Platform.runLater(()->{
                Spinner<Integer> numberSel = new Spinner<>(1,message.getMaxMoves(),1);
                View.makeSpinnerDialog(numberSel, nh, "Move Mother Nature", "How many steps?");
            });
        }
    }



    public void enableIslands(){
        for(int i = 0; i<12;i++){
            if (!map.getAllIslands().get(i).isJoined()){
                islands.get(i).setDisable(false);
            }
        }
    }

    private void initCloudStuds(){
        cloudStuds = new ArrayList<>(
                List.of(
                    //cloud 1
                    new ArrayList<>(List.of(
                                List.of(c1y1, c1r1, c1p1, c1g1, c1b1), List.of(c1y2, c1r2, c1p2, c1g2, c1b2),
                                List.of(c1y3, c1r3, c1p3, c1g3, c1b3), List.of(c1y4, c1r4, c1p4, c1g4, c1b4)
                        )
                    ),
                    //cloud 2
                    new ArrayList<>(List.of(
                                List.of(c2y1, c2r1, c2p1, c2g1, c2b1), List.of(c2y2, c2r2, c2p2, c2g2, c2b2),
                                List.of(c2y3, c2r3, c2p3, c2g3, c2b3), List.of(c2y4, c2r4, c2p4, c2g4, c2b4)
                        )
                    ),
                    //cloud 3
                    new ArrayList<>(List.of(
                                List.of(c3y1, c3r1, c3p1, c3g1, c3b1), List.of(c3y2, c3r2, c3p2, c3g2, c3b2),
                                List.of(c3y3, c3r3, c3p3, c3g3, c3b3), List.of(c3y4, c3r4, c3p4, c3g4, c3b4)
                        )
                    )
                )
        );
        if (numClouds == 2) {
            cloudStuds.remove(2);
            cloudStuds.forEach(cloud->cloud.remove(2));
            c3.setVisible(false);
        }
        cloudStuds.forEach((cloud)->cloud.forEach((pos)->{
            pos.get(0).setOnMouseClicked(this::sendYellow);
            pos.get(1).setOnMouseClicked(this::sendRed);
            pos.get(2).setOnMouseClicked(this::sendPink);
            pos.get(3).setOnMouseClicked(this::sendGreen);
            pos.get(4).setOnMouseClicked(this::sendBlue);
            pos.forEach(img->img.setDisable(true));
        }));

    }

    private void sendBlue(MouseEvent mouseEvent) {
        nh.sendMessage("blue");
    }

    private void sendGreen(MouseEvent mouseEvent) {
        nh.sendMessage("green");
    }

    private void sendPink(MouseEvent mouseEvent) {
        nh.sendMessage("pink");
    }

    private void sendRed(MouseEvent mouseEvent) {
        nh.sendMessage("red");
    }

    private void sendYellow(MouseEvent mouseEvent) {
        nh.sendMessage("yellow");
    }

    private void bindClouds(){
        for (int i = 0; i<numClouds; i++){
            List<Student> cloud = clouds.get(i);
            List<List<ImageView>> toBind = cloudStuds.get(i);
            int maxPos = numPlayers ==3? 4:3;
            for (int pos = 0; pos<maxPos; pos++) {
                    switch (cloud.get(pos)) {
                        case YELLOW -> toBind.get(pos).get(0).setVisible(true);
                        case RED -> toBind.get(pos).get(1).setVisible(true);
                        case PINK -> toBind.get(pos).get(2).setVisible(true);
                        case GREEN -> toBind.get(pos).get(3).setVisible(true);
                        case BLUE -> toBind.get(pos).get(4).setVisible(true);
                    }
            }
        }
    }

    private void bindAllLabels(){
        List<List<Label>> allStudentList =
                List.of(List.of(red0,yellow0,pink0,green0,blue0),List.of(red1,yellow1,pink1,green1,blue1),
                List.of(red2,yellow2,pink2,green2,blue2), List.of(red3,yellow3,pink3,green3,blue3),
                List.of(red4,yellow4,pink4,green4,blue4),List.of(red5,yellow5,pink5,green5,blue5),
                List.of(red6,yellow6,pink6,green6,blue6), List.of(red7,yellow7,pink7,green7,blue7),
                List.of(red8,yellow8,pink8,green8,blue8), List.of(red9,yellow9,pink9,green9,blue9),
                List.of(red10,yellow10,pink10,green10,blue10), List.of(red11,yellow11,pink11,green11,blue11));

        for (Island island: map.getAllIslands()){
            List<Label> listOfIsland = allStudentList.get(island.getId());
            if(island.isJoined()){
                listOfIsland.forEach((label -> label.setVisible(false)));
            }
            else{
                listOfIsland.get(0).setText(island.getStudents().get(Student.RED).toString());
                listOfIsland.get(1).setText(island.getStudents().get(Student.YELLOW).toString());
                listOfIsland.get(2).setText(island.getStudents().get(Student.PINK).toString());
                listOfIsland.get(3).setText(island.getStudents().get(Student.GREEN).toString());
                listOfIsland.get(4).setText(island.getStudents().get(Student.BLUE).toString());
            }
        }
    }
    private void bindMotherNature(){
        List<ImageView> allmn = List.of(mn0,mn1,mn2,mn3,mn4,mn5,mn6,mn7,mn8,mn9,mn10,mn11);
        allmn.forEach((img)->img.setVisible(false));
        allmn.get(map.getMotherNature()).setVisible(true);
        System.out.println("mother nature should be at "+ map.getMotherNature());
    }
    private void addBridges(){
        List<Object> lastJoin = map.getLastJoin();
        if (lastJoin!= null){
            List<List<ImageView>> bridgesFrom = List.of(List.of(b110,b01),List.of(b01,b12),List.of(b12,b23),
                    List.of(b23,b34),List.of(b34,b45),List.of(b45,b56),List.of(b56,b67),List.of(b67,b78),
                    List.of(b78,b89),List.of(b89,b910),List.of(b910,b1011),List.of(b1011,b110));
            List<ImageView> choose = bridgesFrom.get( (Integer)lastJoin.get(0) );
            switch ((String) lastJoin.get(1)) {
                case "left" -> choose.get(0).setVisible(true);
                case "right" -> choose.get(1).setVisible(true);
                case "both" -> {
                    choose.get(0).setVisible(true);
                    choose.get(1).setVisible(true);
                }
            }
        }
    }

    private void showTowers(){
        List<List<ImageView>> allTowers =
                List.of(List.of(tow_0b,tow_0w,tow_0g),List.of(tow_1b,tow_1w,tow_1g), List.of(tow_2b,tow_2w,tow_2g),
                List.of(tow_3b,tow_3w,tow_3g), List.of(tow_4b,tow_4w,tow_4g), List.of(tow_5b,tow_5w,tow_5g),
                List.of(tow_6b,tow_6w,tow_6g),List.of(tow_7b,tow_7w,tow_7g), List.of(tow_8b,tow_8w,tow_8g),
                List.of(tow_9b,tow_9w,tow_9g), List.of(tow_10b,tow_10w,tow_10g), List.of(tow_11b,tow_11w,tow_11g));

        for (Island island: map.getAllIslands()){
            List<ImageView> listOfIsland = allTowers.get(island.getId());
            listOfIsland.forEach((img)->img.setVisible(false));
            if (island.isJoined()){
                continue;
            }
            Player owner = island.getOwner();
            if (owner!=null) {
                TowerColor color = owner.getTowerColor();
                switch (color){
                    case BLACK -> listOfIsland.get(0).setVisible(true);
                    case WHITE -> listOfIsland.get(1).setVisible(true);
                    case GREY -> listOfIsland.get(2).setVisible(true);
                }
            }
        }
    }



    //space for fxml commands
    public void enter0(){
        island0.setEffect(new DropShadow());
    }
    public void exit0(){
        island0.setEffect(null);
    }
    public void send0(){
        nh.sendMessage("0");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter1(){
        island1.setEffect(new DropShadow());
    }
    public void exit1(){
        island1.setEffect(null);
    }
    public void send1(){
        nh.sendMessage("1");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter2(){
        island2.setEffect(new DropShadow());
    }
    public void exit2(){
        island2.setEffect(null);
    }
    public void send2(){
        nh.sendMessage("2");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter3(){
        island3.setEffect(new DropShadow());
    }
    public void exit3(){
        island3.setEffect(null);
    }
    public void send3(){
        nh.sendMessage("3");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter4(){
        island4.setEffect(new DropShadow());
    }
    public void exit4(){
        island4.setEffect(null);
    }
    public void send4() {
        nh.sendMessage("4");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter5(){
        island5.setEffect(new DropShadow());
    }
    public void exit5(){
        island5.setEffect(null);
    }
    public void send5(){
        nh.sendMessage("5");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter6(){
        island6.setEffect(new DropShadow());
    }
    public void exit6(){
        island6.setEffect(null);
    }
    public void send6(){
        nh.sendMessage("6");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter7(){
        island7.setEffect(new DropShadow());
    }
    public void exit7(){
        island7.setEffect(null);
    }
    public void send7(){
        nh.sendMessage("7");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter8(){
        island8.setEffect(new DropShadow());
    }
    public void exit8(){
        island8.setEffect(null);
    }
    public void send8(){
        nh.sendMessage("8");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter9(){
        island9.setEffect(new DropShadow());
    }
    public void exit9(){
        island9.setEffect(null);
    }
    public void send9(){
        nh.sendMessage("9");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter10(){
        island10.setEffect(new DropShadow());
    }
    public void exit10(){
        island10.setEffect(null);
    }
    public void send10(){
        nh.sendMessage("10");
        islands.forEach((i)->i.setDisable(true));
    }

    public void enter11(){
        island11.setEffect(new DropShadow());
    }
    public void exit11(){
        island11.setEffect(null);
    }
    public void send11(){
        nh.sendMessage("11");
        islands.forEach((i)->i.setDisable(true));
    }


    public void enteredStudent(MouseEvent event) {
        ImageView student = (ImageView) event.getSource();
        Bloom effect = new Bloom();
        effect.setInput(new DropShadow());
        student.setEffect(effect);
    }

    public void exitedStudent(MouseEvent event) {
        ImageView student = (ImageView) event.getSource();
        student.setEffect(new DropShadow());
    }
}
