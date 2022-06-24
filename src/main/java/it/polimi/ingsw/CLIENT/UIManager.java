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
                    while (ip==null){
                        try{ip = waitingView.getIp();
                            Thread.sleep(10);}
                        catch (Exception ignored){}
                    }
                    waitingView.askPort();
                    while (port==null){
                        try{port = waitingView.getPort();
                            Thread.sleep(10);}
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
            if (ip.equals("")){ip = "localhost";}
            System.out.println("Insert server connection port (default is 1337).");
            String string = scanner.nextLine();
            int port = Integer.parseInt(string.equals("")? "1337":string);
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

    private View charactersView;
    private Parent charactersRoot;
    public Parent getCharactersRoot() {
        return charactersRoot;
    }
    public View getCharactersView(){
        if (this.charactersView ==null){
            try {
                FXMLLoader actionPhaseLoader = new FXMLLoader(getClass().getResource("/CharactersView.fxml"));
                charactersRoot = actionPhaseLoader.load();
                charactersView = actionPhaseLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return charactersView;
    }

    private View p1SchoolView;
    private Parent p1SchoolRoot;
    public Parent getP1SchoolRoot() {
        return p1SchoolRoot;
    }
    public View getP1SchoolView(){
        if (this.p1SchoolView ==null){
            try {
                FXMLLoader actionPhaseLoader = new FXMLLoader(getClass().getResource("/OtherPlayerView.fxml"));
                p1SchoolRoot = actionPhaseLoader.load();
                p1SchoolView = actionPhaseLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return p1SchoolView;
    }

    private View p2SchoolView;
    private Parent p2SchoolRoot;

    public Parent getP2SchoolRoot() {
        return p2SchoolRoot;
    }
    public View getP2SchoolView(){
        if (this.p2SchoolRoot ==null){
            try {
                FXMLLoader actionPhaseLoader = new FXMLLoader(getClass().getResource("/OtherPlayerView.fxml"));
                p2SchoolRoot = actionPhaseLoader.load();
                p2SchoolView = actionPhaseLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return p2SchoolView;
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
