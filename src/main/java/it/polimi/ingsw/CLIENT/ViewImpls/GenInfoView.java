package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.GenInfoMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.GameMap;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
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

    public ImageView tow_0b;public ImageView tow_0w;public ImageView tow_0g;public ImageView tow_1b;public ImageView tow_1w;public ImageView tow_1g;
    public ImageView tow_2b;public ImageView tow_2w;public ImageView tow_2g;public ImageView tow_3b;public ImageView tow_3w;public ImageView tow_3g;
    public ImageView tow_4b;public ImageView tow_4w;public ImageView tow_4g;public ImageView tow_5b;public ImageView tow_5w;public ImageView tow_5g;
    public ImageView tow_6b;public ImageView tow_6w;public ImageView tow_6g;public ImageView tow_7b;public ImageView tow_7w;public ImageView tow_7g;
    public ImageView tow_8b;public ImageView tow_8w;public ImageView tow_8g;public ImageView tow_9b;public ImageView tow_9w;public ImageView tow_9g;
    public ImageView tow_10w;public ImageView tow_10g;public ImageView tow_11b;public ImageView tow_11w;public ImageView tow_11g;public ImageView tow_10b;



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



    //This thing is ugly as all hell, but at least it works
    private void bindAllLabels(){
        red0.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(0).getStudents().get(Student.RED).toString()));
        yellow0.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(0).getStudents().get(Student.YELLOW).toString()));
        pink0.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(0).getStudents().get(Student.PINK).toString()));
        green0.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(0).getStudents().get(Student.GREEN).toString()));
        blue0.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(0).getStudents().get(Student.BLUE).toString()));

        red1.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(1).getStudents().get(Student.RED).toString()));
        yellow1.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(1).getStudents().get(Student.YELLOW).toString()));
        pink1.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(1).getStudents().get(Student.PINK).toString()));
        green1.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(1).getStudents().get(Student.GREEN).toString()));
        blue1.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(1).getStudents().get(Student.BLUE).toString()));

        red2.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(2).getStudents().get(Student.RED).toString()));
        yellow2.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(2).getStudents().get(Student.YELLOW).toString()));
        pink2.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(2).getStudents().get(Student.PINK).toString()));
        green2.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(2).getStudents().get(Student.GREEN).toString()));
        blue2.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(2).getStudents().get(Student.BLUE).toString()));

        red3.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(3).getStudents().get(Student.RED).toString()));
        yellow3.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(3).getStudents().get(Student.YELLOW).toString()));
        pink3.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(3).getStudents().get(Student.PINK).toString()));
        green3.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(3).getStudents().get(Student.GREEN).toString()));
        blue3.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(3).getStudents().get(Student.BLUE).toString()));

        red4.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(4).getStudents().get(Student.RED).toString()));
        yellow4.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(4).getStudents().get(Student.YELLOW).toString()));
        pink4.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(4).getStudents().get(Student.PINK).toString()));
        green4.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(4).getStudents().get(Student.GREEN).toString()));
        blue4.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(4).getStudents().get(Student.BLUE).toString()));

        red5.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(5).getStudents().get(Student.RED).toString()));
        yellow5.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(5).getStudents().get(Student.YELLOW).toString()));
        pink5.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(5).getStudents().get(Student.PINK).toString()));
        green5.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(5).getStudents().get(Student.GREEN).toString()));
        blue5.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(5).getStudents().get(Student.BLUE).toString()));

        red6.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(6).getStudents().get(Student.RED).toString()));
        yellow6.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(6).getStudents().get(Student.YELLOW).toString()));
        pink6.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(6).getStudents().get(Student.PINK).toString()));
        green6.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(6).getStudents().get(Student.GREEN).toString()));
        blue6.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(6).getStudents().get(Student.BLUE).toString()));

        red7.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(7).getStudents().get(Student.RED).toString()));
        yellow7.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(7).getStudents().get(Student.YELLOW).toString()));
        pink7.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(7).getStudents().get(Student.PINK).toString()));
        green7.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(7).getStudents().get(Student.GREEN).toString()));
        blue7.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(7).getStudents().get(Student.BLUE).toString()));

        red8.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(8).getStudents().get(Student.RED).toString()));
        yellow8.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(8).getStudents().get(Student.YELLOW).toString()));
        pink8.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(8).getStudents().get(Student.PINK).toString()));
        green8.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(8).getStudents().get(Student.GREEN).toString()));
        blue8.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(8).getStudents().get(Student.BLUE).toString()));

        red9.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(9).getStudents().get(Student.RED).toString()));
        yellow9.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(9).getStudents().get(Student.YELLOW).toString()));
        pink9.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(9).getStudents().get(Student.PINK).toString()));
        green9.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(9).getStudents().get(Student.GREEN).toString()));
        blue9.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(9).getStudents().get(Student.BLUE).toString()));

        red10.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(10).getStudents().get(Student.RED).toString()));
        yellow10.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(10).getStudents().get(Student.YELLOW).toString()));
        pink10.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(10).getStudents().get(Student.PINK).toString()));
        green10.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(10).getStudents().get(Student.GREEN).toString()));
        blue10.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(10).getStudents().get(Student.BLUE).toString()));

        red11.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(11).getStudents().get(Student.RED).toString()));
        yellow11.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(11).getStudents().get(Student.YELLOW).toString()));
        pink11.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(11).getStudents().get(Student.PINK).toString()));
        green11.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(11).getStudents().get(Student.GREEN).toString()));
        blue11.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(11).getStudents().get(Student.BLUE).toString()));
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

    }
}
