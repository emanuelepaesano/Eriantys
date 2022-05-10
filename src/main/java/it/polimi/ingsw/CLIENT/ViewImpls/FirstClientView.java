package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FirstClientView implements View {
    public TextArea textArea;
    public Button two;
    public Button three;
    public AnchorPane fcPane;
    Stage stage;
    NetworkHandler nh;



    public void initialize(){
        nh = UIManager.getGuiManager().getNh();
        nh.setMessageArrivedObserver((msg)-> {
                    if (msg.getView().equals("simpleview")) {
                        textArea.appendText(""+ msg);
                    } else {
                        Platform.runLater(()->UIManager.getGuiManager().selectAndFillView(msg));
                    }
                }
        );
    }
    public void send2(ActionEvent actionEvent) {
        nh.sendMessage(new StringMessage("2"));
    }

    public void send3(ActionEvent actionEvent) {
        nh.sendMessage(new StringMessage("3"));
    }

    @Override
    public void display(Parent root) {
        stage = UIManager.getGuiManager().getMainWindow();
        Scene sc = new Scene(root);
        stage.setScene(sc);
        stage.setTitle("Eriantys");
        stage.sizeToScene();
        stage.show();
    }

    @Override
    public void display() {

    }

    @Override
    public void sendReply() {

    }

    @Override
    public void fillInfo(Message message) {

    }
}
