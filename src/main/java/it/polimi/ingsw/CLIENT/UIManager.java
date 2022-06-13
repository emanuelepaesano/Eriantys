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
import java.util.Scanner;

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

    private final Scanner scanner = new Scanner(System.in);

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
                    String ip = null;
                    Integer port = null;
                    System.out.println("inizio a provare");
                    while (ip==null){
                        try{ip = waitingView.getIp();
                            System.out.println("");}
                        catch (Exception ignored){}
                    }
                    waitingView.askPort();
                    while (port==null){
                        try{port = waitingView.getPort();
                            System.out.println("");}
                        catch (Exception ignored){}
                    }
                    waitingView.startWaiting();
                    nh.startConnection(ip,port);
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
        else {
            Platform.exit();
            System.out.println("Starting connection to game server.\n" +
            "Please insert server ip address, or type \"localhost\" for local game.");
            String ip = scanner.nextLine();
            System.out.println("Insert server connection port (default is 1337).");
            int port = Integer.parseInt(scanner.nextLine());
            cliView = new CLIView(nh);
            nh.startConnection(ip ,port);
            nh.startListenerThread();
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

    public void startWaitingView() throws IOException {
        FXMLLoader waitLoader = new FXMLLoader(getClass().getResource("/WaitingView.fxml"));
        Parent waitingRoot = waitLoader.load();
        waitingView = waitLoader.getController();
        waitingView.display(waitingRoot);
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

    private View schoolView;
    private Parent schoolRoot;

    public Parent getSchoolRoot() {
        return schoolRoot;
    }
    public View getSchoolView(){
        if (this.schoolView ==null){
            try {
                FXMLLoader actionPhaseLoader = new FXMLLoader(getClass().getResource("/SchoolView.fxml"));
                schoolRoot = actionPhaseLoader.load();
                schoolView = actionPhaseLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return schoolView;
    }

    private View endGameView;
    private Parent endGameRoot;

    public Parent getEndGameRoot() {
        return endGameRoot;
    }
    public View getEndGameView(){
        if (this.endGameView ==null){
            try {
                FXMLLoader actionPhaseLoader = new FXMLLoader(getClass().getResource("/EndGameView.fxml"));
                endGameRoot = actionPhaseLoader.load();
                endGameView = actionPhaseLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return endGameView;
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
