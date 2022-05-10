package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.CLIENT.ViewImpls.CLIView;
import it.polimi.ingsw.CLIENT.ViewImpls.LoginView;
import it.polimi.ingsw.CLIENT.ViewImpls.WaitingView;
import it.polimi.ingsw.messages.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UIManager extends Application{
    private static UIManager GUIManager;
    private static Stage mainWindow;
    private static WaitingView waitingView;
    private View loginView;
    private static NetworkHandler nh;
    private Boolean GUI;
    private static View cliView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        chooseUI();
        nh = new NetworkHandler(GUI);
        if (GUI){
            Platform.setImplicitExit(false);
            GUIManager = this;
            mainWindow = stage;
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
                        Platform.runLater(()->selectAndFillView(msg))
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

    void selectAndFillView(Message message){
        switch (message.getView()){
            case "loginview":
                getLoginView().fillInfo(message);
                loginView.display(loginRoot);
            case "planningview": //from planningphasemessage
            case "actionview": //from actionphasemessage
            case "cloudselection": //from cloudmessage
            case "generalview": //from generalviewmessage
            case "simpleview": //from stringmessage //this can just be an overlaying popup or something similar
        }
    }

    public void startWaitingView(){
        waitingView = new WaitingView();
        waitingView.display();
    }

    private Parent loginRoot;
    public View getLoginView(){
        if (this.loginView ==null){
            try {
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
                loginRoot = loginLoader.load();
                loginView = loginLoader.getController();
            }catch(Exception ex){}
        }
        return loginView;
    }


    public static UIManager getGuiManager(){
        return GUIManager;
    }

    public static Stage getMainWindow() {
        return mainWindow;
    }

    public static WaitingView getWaitingView() {
        return waitingView;
    }

    public static NetworkHandler getNh() {
        return nh;
    }

    public static View getCliView() {
        return cliView;
    }
}
