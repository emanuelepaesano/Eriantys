package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FirstClientView implements View {
    public Button two;
    public Button three;
    public AnchorPane fcPane;
    public TextField testo;
    public Button normal;
    public Button expert;
    Stage stage;
    NetworkHandler nh;



    public void initialize(){
        nh = UIManager.getUIManager().getNh();
        nh.setMessageArrivedObserver((msg)-> {
                    if (msg.getClass().getSimpleName().equals("FirstClientMessage")) {
                        testo.setText("Do you want Normal game or Expert variant?");
                        two.setDisable(true); two.setVisible(false);
                        three.setDisable(true); three.setVisible(false);
                        normal.setVisible(true); normal.setDisable(false);
                        expert.setVisible(true); expert.setDisable(false);
                    }
                    else if (msg.getClass().getSimpleName().equals("NoReplyMessage")){
                        testo.setText(msg.toString());
                    }
                    else {
                        Platform.runLater(msg::switchAndFillView);
                    }
        }
        );
    }

    public void sendNormal(){nh.sendReply("normal");}
    public void sendExpert(){nh.sendReply("expert");}
    public void send2(ActionEvent actionEvent) {
        nh.sendReply(("2"));
    }

    public void send3(ActionEvent actionEvent) {
        nh.sendReply(("3"));
    }

    @Override
    public void display(Parent root) {
        stage = UIManager.getUIManager().getMainWindow();
        Scene sc;
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
    public void display() {
    }

    @Override
    public void sendReply() {

    }

    @Override
    public void fillInfo(Message message) {

    }
}
