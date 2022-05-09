package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.CLIENT.ViewImpls.CLIView;
import it.polimi.ingsw.CLIENT.ViewImpls.LoginView;
import it.polimi.ingsw.CLIENT.ViewImpls.WaitingView;
import it.polimi.ingsw.messages.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
    private UserView view;
    private NetworkHandler nh;
    private Consumer<Message> messageArrivedObserver;
    private List<Message> delayedMessages = new ArrayList<>();
    private Boolean GUI;
    private View cliView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        chooseUI();
        view = new UserView();
        nh = new NetworkHandler(GUI,view);
        if (GUI){
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
                nh.setMessageArrivedObserver((msg)-> UIManager.getGuiManager().selectAndFillView(msg, nh));
                nh.startListenerThread();
            });
            new Thread(task).start();
        }
        else {cliView = new CLIView(nh);
            StartCLI();
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
    void StartCLI(){
        if(!GUI){view.setCurrentView(this.cliView);}
    }

    void selectAndFillView(Message message, NetworkHandler nh){
        switch (message.getView()){
            case "loginview":
                getLoginView().fillInfo(message);
                System.out.println("trying to enable login view");
                loginView.display();
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

    public View getLoginView(){
        if (this.loginView ==null){
            loginView = new LoginView();
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
}
