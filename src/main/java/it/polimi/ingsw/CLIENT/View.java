package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.messages.Message;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public interface View extends Serializable {

    static void makeSpinnerDialog(Spinner<Integer> numberSel, NetworkHandler nh, String title, String header) {
        AtomicBoolean sent = new AtomicBoolean(false);
        numberSel.setEditable(true);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        GridPane grid = new GridPane();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        Button send = new Button();
        send.setText("Send");
        send.setOnAction((e)->{
            nh.sendReply(numberSel.getValue().toString());
            sent.set(true);
            dialog.close();
        });
        grid.add(numberSel,0,0);
        grid.add(send,0,1);
        dialog.getDialogPane().setContent(grid);
        dialog.initModality(Modality.NONE);
        Optional<ButtonType> result = dialog.showAndWait();
        if (!sent.get() && result.isPresent()) {
            makeSpinnerDialog(numberSel,nh,title,header);
        }
    }

    void display();
    void sendReply();
    void fillInfo(Message message);

}
