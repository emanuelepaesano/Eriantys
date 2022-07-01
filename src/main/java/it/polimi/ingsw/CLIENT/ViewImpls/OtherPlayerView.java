package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.UIManager;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.TowerColor;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.Assistant.*;

public class OtherPlayerView implements View {

    public ImageView e1b; public ImageView e1g; public ImageView e1p; public ImageView e1r; public ImageView e1y;
    public ImageView e2b; public ImageView e2g; public ImageView e2p; public ImageView e2r; public ImageView e2y;
    public ImageView e3b; public ImageView e3g; public ImageView e3p; public ImageView e3r; public ImageView e3y;
    public ImageView e4b; public ImageView e4g; public ImageView e4p; public ImageView e4r; public ImageView e4y;
    public ImageView e5b; public ImageView e5g; public ImageView e5p; public ImageView e5r; public ImageView e5y;
    public ImageView e6b; public ImageView e6g; public ImageView e6p; public ImageView e6r; public ImageView e6y;
    public ImageView e7b; public ImageView e7g; public ImageView e7p; public ImageView e7r; public ImageView e7y;
    public ImageView e8b; public ImageView e8g; public ImageView e8p; public ImageView e8r; public ImageView e8y;
    public ImageView e9b; public ImageView e9g; public ImageView e9p; public ImageView e9r; public ImageView e9y;

    public ImageView dg1; public ImageView dg2; public ImageView dg3; public ImageView dg4; public ImageView dg5;
    public ImageView dg6; public ImageView dg7; public ImageView dg8; public ImageView dg9; public ImageView dg10;
    public ImageView dr1; public ImageView dr2; public ImageView dr3; public ImageView dr4; public ImageView dr5;
    public ImageView dr6; public ImageView dr7; public ImageView dr8; public ImageView dr9; public ImageView dr10;
    public ImageView dy1; public ImageView dy2; public ImageView dy3; public ImageView dy4; public ImageView dy5;
    public ImageView dy6; public ImageView dy7; public ImageView dy8; public ImageView dy9; public ImageView dy10;
    public ImageView dp1; public ImageView dp2; public ImageView dp3; public ImageView dp4; public ImageView dp5;
    public ImageView dp6; public ImageView dp7; public ImageView dp8; public ImageView dp9; public ImageView dp10;
    public ImageView db1; public ImageView db2; public ImageView db3; public ImageView db4; public ImageView db5;
    public ImageView db6; public ImageView db7; public ImageView db8; public ImageView db9; public ImageView db10;

    public ImageView pg; public ImageView pr; public ImageView py; public ImageView pp; public ImageView pb;

    public ImageView t1w; public ImageView t1b; public ImageView t1g;
    public ImageView t2w; public ImageView t2b; public ImageView t2g;
    public ImageView t3w; public ImageView t3b; public ImageView t3g;
    public ImageView t4w; public ImageView t4b; public ImageView t4g;
    public ImageView t5w; public ImageView t5b; public ImageView t5g;
    public ImageView t6w; public ImageView t6b; public ImageView t6g;
    public ImageView t7w; public ImageView t7b; public ImageView t7g;
    public ImageView t8w; public ImageView t8b; public ImageView t8g;

    public ImageView one;
    public ImageView two;
    public ImageView three;
    public ImageView four;
    public ImageView five;
    public ImageView six;
    public ImageView seven;
    public ImageView eight;
    public ImageView nine;
    public ImageView ten;
    public Text towLabel;
    public Text playerName;
    Player player;
    Stage stage;
    NetworkHandler nh;
    List<ImageView> assistantImageViewList;
    List<List<ImageView>> entranceImageViewList;
    Map<Student, List<ImageView>> diningRoomImageViewMap;
    Map<Student, ImageView> professorsImageViewList;
    Map<TowerColor, List<ImageView>> towersImageViewList;
    List<Assistant> allAssistants;
    Map<Assistant, Boolean> playersAssistants;

    @Override
    public void display() {}

    public void initialize(){
        entranceImageViewList =
                List.of(List.of(e1b, e1g, e1p, e1r, e1y), List.of(e2b, e2g, e2p, e2r, e2y),
                List.of(e3b, e3g, e3p, e3r, e3y), List.of(e4b, e4g, e4p, e4r, e4y),
                List.of(e5b, e5g, e5p, e5r, e5y), List.of(e6b, e6g, e6p, e6r, e6y),
                List.of(e7b, e7g, e7p, e7r, e7y), List.of(e8b, e8g, e8p, e8r, e8y),
                List.of(e9b, e9g, e9p, e9r, e9y));
        diningRoomImageViewMap =
                Map.of(Student.GREEN, List.of(dg1, dg2, dg3, dg4, dg5, dg6, dg7, dg8, dg9, dg10),
                Student.RED, List.of(dr1, dr2, dr3, dr4, dr5, dr6, dr7, dr8, dr9, dr10),
                Student.YELLOW, List.of(dy1, dy2, dy3, dy4, dy5, dy6, dy7, dy8, dy9, dy10),
                Student.PINK, List.of(dp1, dp2, dp3, dp4, dp5, dp6, dp7, dp8, dp9, dp10),
                Student.BLUE, List.of(db1, db2, db3, db4, db5, db6, db7, db8, db9, db10));
        professorsImageViewList =
                Map.of(Student.GREEN, pg, Student.RED, pr, Student.YELLOW, py, Student.PINK, pp, Student.BLUE, pb);
        towersImageViewList=
                Map.of(TowerColor.WHITE, List.of(t1w, t2w, t3w, t4w, t5w, t6w, t7w, t8w),
                TowerColor.BLACK, List.of(t1b, t2b, t3b, t4b, t5b, t6b, t7b, t8b),
                TowerColor.GREY, List.of(t1g, t2g, t3g, t4g, t5g, t6g, t7g, t8g));

        this.nh = UIManager.getUIManager().getNh();
        entranceImageViewList.forEach(list->{
            list.get(0).setOnMouseClicked(this::blueSelected);
            list.get(1).setOnMouseClicked(this::greenSelected);
            list.get(2).setOnMouseClicked(this::pinkSelected);
            list.get(3).setOnMouseClicked(this::redSelected);
            list.get(4).setOnMouseClicked(this::yellowSelected);
            list.forEach(img->img.setDisable(true));
        });

//        show remaining assistants
        allAssistants = List.of(ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,TEN);
        assistantImageViewList = List.of(one, two, three, four, five, six, seven, eight, nine, ten);
    }



    @Override
    public void fillInfo(Message mes) {
    }

    public void fillInfoWithPlayer(Player player) {
        this.player = player;
        playerName.setText(this.player.getPlayerName());
        playersAssistants = this.player.getAssistants();

        //Here we have to link the elements from the model to the graphic components.
        bindEntrance();
        bindDiningRoom();
        bindProfessors();
        bindTowers();
        bindAssistants();
    }

    public void setEntranceInvisible() {
        for (List<ImageView> l: entranceImageViewList) {
            for (ImageView iv: l) {
                iv.setVisible(false);
            }
        }
    }

    public void bindEntrance() {
        setEntranceInvisible();
        int index = 0;
        entranceImageViewList.forEach(list->list.forEach(s->s.setVisible(false)));
        for (Student s: player.getEntrance().getStudents()) {
            if (s == Student.BLUE) {
                entranceImageViewList.get(index).get(0).setVisible(true);
                index++;
                continue;
            } else if (s == Student.GREEN) {
                entranceImageViewList.get(index).get(1).setVisible(true);
                index++;
                continue;
            } else if (s == Student.PINK) {
                entranceImageViewList.get(index).get(2).setVisible(true);
                index++;
                continue;
            } else if (s == Student.RED) {
                entranceImageViewList.get(index).get(3).setVisible(true);
                index++;
                continue;
            } else if (s == Student.YELLOW) {
                entranceImageViewList.get(index).get(4).setVisible(true);
                index++;
                continue;
            }
        }
    }

    public void setDiningRoomInvisible() {
        for (List<ImageView> l: diningRoomImageViewMap.values()) {
            for (ImageView iv: l) {
                iv.setVisible(false);
            }
        }
    };

    public void bindDiningRoom() {
        setDiningRoomInvisible();
        Map<Student, Integer> table = player.getDiningRoom().getTables();
        for (Student s: diningRoomImageViewMap.keySet()) {
            for (int i=0; i<table.get(s); i++) {
                diningRoomImageViewMap.get(s).get(i).setVisible(true);
            }
        }
    }

    public void setProfessorsInvisible() {
        for (ImageView iv: professorsImageViewList.values()) {
            iv.setVisible(false);
        }
    }

    public void bindProfessors() {
        setProfessorsInvisible();
        Map<Student, Boolean> professors = player.getDiningRoom().getProfessors();
        for (Student s: professors.keySet()) {
            if (professors.get(s) == true) {
                professorsImageViewList.get(s).setVisible(true);
            }
        }
    }

    public void setTowersInvisible() {
        for (List<ImageView> l: towersImageViewList.values()) {
            for (ImageView iv: l) {
                iv.setVisible(false);
            }
        }
    }

    public void bindTowers() {
        setTowersInvisible();
        towLabel.setText("Towers: " + player.getNumTowers().toString());

        TowerColor towerColor = player.getTowerColor();
        Integer towerNum = player.getNumTowers();
        List<ImageView> towersImageView = towersImageViewList.get(towerColor);
        for (int i=0; i<towerNum; i++) {
            towersImageView.get(i).setVisible(true);
        }
    }

    public void enteredStudent(MouseEvent event){
        ImageView student = (ImageView) event.getSource();
        Bloom effect = new Bloom();
        effect.setInput(new DropShadow());
        student.setEffect(effect);
    }

    public void exitedStudent(MouseEvent event){
        ImageView student = (ImageView) event.getSource();
        student.setEffect(new DropShadow());
    }


    private void yellowSelected(MouseEvent mouseEvent) {
        nh.sendReply("yellow");
        entranceImageViewList.forEach(list -> list.forEach(img -> img.setDisable(true)));

    }

    private void redSelected(MouseEvent mouseEvent) {
        nh.sendReply("red");
        entranceImageViewList.forEach(list -> list.forEach(img -> img.setDisable(true)));
    }

    private void pinkSelected(MouseEvent mouseEvent) {
        nh.sendReply("pink");
        entranceImageViewList.forEach(list -> list.forEach(img -> img.setDisable(true)));
    }

    private void greenSelected(MouseEvent mouseEvent) {
        nh.sendReply("green");
        entranceImageViewList.forEach(list -> list.forEach(img -> img.setDisable(true)));
    }

    private void blueSelected(MouseEvent mouseEvent) {
        nh.sendReply("blue");
        entranceImageViewList.forEach(list -> list.forEach(img -> img.setDisable(true)));
    }

    private void bindAssistants(){
        for (int i=0; i<10;i++){
            Assistant assistant = allAssistants.get(i);
            if (playersAssistants.get(assistant) == false){
                assistantImageViewList.get(i).setVisible(false);
            }
        }
    }

}