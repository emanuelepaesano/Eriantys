package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.GenInfoMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;

public class GenInfoView implements View {

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



    GameMap map;
    List<Player> players;
    Stage stage;

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

    @Override
    public void fillInfo(Message mes) {
        GenInfoMessage message = (GenInfoMessage) mes;
        map = message.getMap();
        players = message.getPlayers();

        //Here we have to link the elements from the model to the graphic components.
        bindAllLabels();
        bindMotherNature();
        addBridges();
        showTowers();
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




}
