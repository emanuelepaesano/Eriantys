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
    private View cliView; private View firstClientView; private View islandView;
    private View planningPhaseView; private View schoolView; private View charactersView;
    private View p2SchoolView; private View p1SchoolView; private View endGameView;
    private Parent loginRoot; private Parent islandRoot; private Parent planningPhaseRoot;
    private Parent firstClientRoot; private Parent schoolRoot; private Parent p1SchoolRoot;
    private Parent p2SchoolRoot; private Parent endGameRoot; private Parent switcherRoot; private Parent charactersRoot;
    private Switcher switcher;
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
            mainWindow.setOnCloseRequest((event)->System.exit(0));
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
    public View getIslandView(){
        if (islandView ==null){
            try {
                System.out.println("i'm making a new controller");
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/IslandView.fxml"));
                islandRoot = loginLoader.load();
                islandView = loginLoader.getController();
            }catch(IOException ex){ex.printStackTrace();}
        }
        return islandView;
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

    public static UIManager getUIManager(){return UIManager;}
    public  Stage getMainWindow() {return mainWindow;}
    public  NetworkHandler getNh() {return nh;}
    public  View getCliView() {return cliView;}
    public Parent getLoginRoot() {return loginRoot;}
    public Parent getSwitcherRoot() {return switcherRoot;}
    public Parent getEndGameRoot() {return endGameRoot;}
    public Parent getP2SchoolRoot() {return p2SchoolRoot;}
    public Parent getP1SchoolRoot() {return p1SchoolRoot;}
    public Parent getCharactersRoot() {return charactersRoot;}
    public Parent getSchoolRoot() {return schoolRoot;}
    public Parent getFirstClientRoot() {return firstClientRoot;}
    public Parent getPlanningPhaseRoot() {return planningPhaseRoot;}
    public Parent getIslandRoot() {return islandRoot;}
}
