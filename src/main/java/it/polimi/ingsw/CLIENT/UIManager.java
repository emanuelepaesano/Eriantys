package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.CLIENT.ViewImpls.CLIView;
import it.polimi.ingsw.CLIENT.ViewImpls.Switcher;
import it.polimi.ingsw.CLIENT.ViewImpls.WaitingView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class UIManager extends Application{
    private static UIManager UIManager;
    private Stage mainWindow;
    private  WaitingView waitingView;
    private View loginView;
    private NetworkHandler nh;
    private Boolean GUI;
    private View cliView;
    private View firstClientView;
    private View genInfoView;

    private View planningPhaseView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        UIManager = this;
        chooseUI();
        nh = new NetworkHandler(GUI);
        if (GUI){
            Platform.setImplicitExit(false);
            mainWindow = stage;
            mainWindow.setResizable(false);
            startWaitingView();
            Task<Void> task = new Task<>() {
                @Override
                public Void call(){
                    nh.startConnection();
                    return null;
                }
            };
            task.setOnSucceeded((e)->{
                waitingView.close();
                nh.setMessageArrivedObserver((msg)->
                        Platform.runLater(msg::switchAndFillView)
                );
                nh.startListenerThread();
            });
            new Thread(task).start();
        }
        else {cliView = new CLIView(nh);
            nh.startConnection();
            nh.startListenerThread();
            Platform.exit();
        }
    }

    void chooseUI(){
        Stage stage = new Stage();
        stage.setTitle("Choose an interface type.");
        Button b1 = new Button("Graphic Interface");
        Button b2 = new Button("Command Line");
        HBox layout = new HBox(b1,b2);
        Scene scene = new Scene(layout,300,300);
        stage.setScene(scene);
        stage.sizeToScene();
        b1.setOnAction(e-> {GUI =true; stage.close();});
        b2.setOnAction(e-> {GUI =false; stage.close();});
        stage.showAndWait();
    }

    public void startWaitingView(){
        waitingView = new WaitingView();
        waitingView.display();
    }

    private Parent loginRoot;
    public Parent getLoginRoot() {
        return loginRoot;
    }

    public View getLoginView(){
        if (this.loginView ==null){
            try {
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
                loginRoot = loginLoader.load();
                loginView = loginLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return loginView;
    }

    private Parent genInfoRoot;

    public Parent getGenInfoRoot() {
        return genInfoRoot;
    }

    public View getGenInfoView(){
        if (genInfoView ==null){
            try {
                System.out.println("i'm making a new controller");
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/GenInfoView.fxml"));
                genInfoRoot = loginLoader.load();
                genInfoView = loginLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return genInfoView;
    }

    private Parent planningPhaseRoot;

    public Parent getPlanningPhaseRoot() {
        return planningPhaseRoot;
    }


    public View getPlanningPhaseView(){
        if (this.planningPhaseView ==null){
            try {
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/PlanningPhaseView.fxml"));
                planningPhaseRoot = loginLoader.load();
                planningPhaseView = loginLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return planningPhaseView;
    }

    private Parent firstClientRoot;
    public Parent getFirstClientRoot() {
        return firstClientRoot;
    }


    public View getFirstClientView(){
        if (this.firstClientView ==null){
            try {
                FXMLLoader firstClientLoader = new FXMLLoader(getClass().getResource("/FirstClientView.fxml"));
                firstClientRoot = firstClientLoader.load();
                firstClientView = firstClientLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return firstClientView;
    }

    private View actionPhaseView;
    private Parent actionPhaseRoot;

    public Parent getActionPhaseRoot() {
        return actionPhaseRoot;
    }
    public View getActionPhaseView(){
        if (this.actionPhaseView ==null){
            try {
                FXMLLoader actionPhaseLoader = new FXMLLoader(getClass().getResource("/SchoolView.fxml"));
                actionPhaseRoot = actionPhaseLoader.load();
                actionPhaseView = actionPhaseLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return actionPhaseView;
    }


    private Switcher switcher;
    private Parent switcherRoot;

    public Parent getSwitcherRoot() {
        return switcherRoot;
    }

    public Switcher getSwitcher() {
        if (this.switcher ==null){
            try {
                FXMLLoader switcherLoader = new FXMLLoader(getClass().getResource("/Switcher.fxml"));
                switcherRoot = switcherLoader.load();
                switcher = switcherLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return switcher;
    }

    public static UIManager getUIManager(){
        return UIManager;
    }

    public  Stage getMainWindow() {
        return mainWindow;
    }

    public  WaitingView getWaitingView() {
        return waitingView;
    }

    public  NetworkHandler getNh() {
        return nh;
    }

    public  View getCliView() {
        return cliView;
    }
}
