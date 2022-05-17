package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.GenInfoMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.GameMap;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class GenInfoView implements View {

    public Image island1;
    public Label green0;
    GameMap map;
    List<Player> players;
    Stage stage;

private ObjectProperty<GameMap> mapProperty = new SimpleObjectProperty<>();

    public GameMap getMapProperty() {
        return mapProperty.get();
    }
    public ObjectProperty<GameMap> mapProperty() {
        return mapProperty;
    }

    public void setMapProperty(GameMap mapProperty) {
        this.mapProperty.set(mapProperty);
    }

    @Override
    public void display() {
        Parent root = UIManager.getUIManager().getGenInfoRoot();
        Scene sc;
        stage = UIManager.getUIManager().getMainWindow();
        if (root.getScene() == null) {
            sc = new Scene(root);
        }
        else sc = root.getScene();
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
        mapProperty.set(map);
        green0.textProperty().bind(new SimpleObjectProperty<>(map.getIslandById(0).getStudents().get(Student.GREEN).toString()));
        players = message.getPlayers();
        //Here we have to link the elements from the model to the graphic components.
    }

}
