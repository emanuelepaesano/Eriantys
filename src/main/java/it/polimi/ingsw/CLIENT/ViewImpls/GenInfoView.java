package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.GenInfoMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.GameMap;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class GenInfoView implements View {
    public Image island1;
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

    GameMap map;
    List<Player> players;
    Stage stage;

    ObjectProperty<Map<Student, Integer>> island0prop = new SimpleObjectProperty<>();

    public Map<Student, Integer> getIsland0prop() {
        return island0prop.get();
    }

    public ObjectProperty<Map<Student, Integer>> island0propProperty() {
        return island0prop;
    }

    ObjectProperty<Map<Student, Integer>> island1prop = new SimpleObjectProperty<>();

    public Map<Student, Integer> getIsland1prop() {
        return island1prop.get();
    }

    public ObjectProperty<Map<Student, Integer>> island1propProperty() {
        return island1prop;
    }

    ObjectProperty<Map<Student, Integer>> island2prop = new SimpleObjectProperty<>();

    public Map<Student, Integer> getIsland2prop() {
        return island2prop.get();
    }

    public ObjectProperty<Map<Student, Integer>> island2propProperty() {
        return island2prop;
    }

    ObjectProperty<Map<Student, Integer>> island3prop = new SimpleObjectProperty<>();

    public Map<Student, Integer> getIsland3prop() {
        return island3prop.get();
    }

    public ObjectProperty<Map<Student, Integer>> island3propProperty() {
        return island3prop;
    }

    ObjectProperty<Map<Student, Integer>> island4prop = new SimpleObjectProperty<>();

    public Map<Student, Integer> getIsland4prop() {
        return island4prop.get();
    }

    public ObjectProperty<Map<Student, Integer>> island4propProperty() {
        return island4prop;
    }

    ObjectProperty<Map<Student, Integer>> island5prop = new SimpleObjectProperty<>();

    public Map<Student, Integer> getIsland5prop() {
        return island5prop.get();
    }

    public ObjectProperty<Map<Student, Integer>> island5propProperty() {
        return island5prop;
    }

    ObjectProperty<Map<Student, Integer>> island6prop = new SimpleObjectProperty<>();
    ObjectProperty<Map<Student, Integer>> island7prop = new SimpleObjectProperty<>();
    ObjectProperty<Map<Student, Integer>> island8prop = new SimpleObjectProperty<>();
    ObjectProperty<Map<Student, Integer>> island9prop = new SimpleObjectProperty<>();
    ObjectProperty<Map<Student, Integer>> island10prop = new SimpleObjectProperty<>();
    ObjectProperty<Map<Student, Integer>> island11prop = new SimpleObjectProperty<>();


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
        linkIslands();
        bindAllLabels();
    }



    private void linkIslands() {
        island0prop.set(map.getIslandById(0).getStudents());
        island1prop.set(map.getIslandById(1).getStudents());
        island2prop.set(map.getIslandById(2).getStudents());
        island3prop.set(map.getIslandById(3).getStudents());
        island4prop.set(map.getIslandById(4).getStudents());
        island5prop.set(map.getIslandById(5).getStudents());
        island6prop.set(map.getIslandById(6).getStudents());
        island7prop.set(map.getIslandById(7).getStudents());
        island8prop.set(map.getIslandById(8).getStudents());
        island9prop.set(map.getIslandById(9).getStudents());
        island10prop.set(map.getIslandById(10).getStudents());
        island11prop.set(map.getIslandById(11).getStudents());


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
}
