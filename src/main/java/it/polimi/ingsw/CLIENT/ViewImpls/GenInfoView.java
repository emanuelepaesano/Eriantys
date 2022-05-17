package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.GenInfoMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.GameMap;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class GenInfoView implements View {

    public ImageView imageView;
    GameMap map;
    List<Player> players;
    Stage stage;

private ObjectProperty<List<Island>> archipelago = new SimpleObjectProperty<>();

    public List<Island> getArchipelago() {
        return archipelago.get();
    }
    public ObjectProperty<List<Island>> archipelagoProperty() {
        return archipelago;
    }

    @Override
    public void display() {
    }

    @Override
    public void display(Parent root) {
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
    public void fillInfo(Message message) {
        GenInfoMessage mess = (GenInfoMessage) message;
        map = mess.getMap();
        players = mess.getPlayers();
        //Here we have to link the elements from the model to the graphic components.

    }

}
