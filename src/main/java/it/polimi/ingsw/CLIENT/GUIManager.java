package it.polimi.ingsw.CLIENT;

import it.polimi.ingsw.CLIENT.ViewImpls.LoginView;
import it.polimi.ingsw.CLIENT.ViewImpls.WaitingView;
import it.polimi.ingsw.messages.Message;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GUIManager{
    private static GUIManager guiManager;
    private static Stage mainWindow;
    private static WaitingView waitingView;
    private View loginView;
    private UserView view;
    private NetworkHandler nh;
    private Consumer<Message> messageArrivedObserver;
    private List<Message> delayedMessages = new ArrayList<>();


    public GUIManager(Stage stage){
        mainWindow = stage;
        guiManager = this;
    }

    void selectAndFillView(Message message, NetworkHandler nh){
        switch (message.getView()){
            case "loginview":
                loginView.fillInfo(message);
                System.out.println("trying to enable login view");
                enableLoginView();
            case "planningview": //from planningphasemessage
            case "actionview": //from actionphasemessage
            case "cloudselection": //from cloudmessage
            case "generalview": //from generalviewmessage
            case "simpleview": //from stringmessage //this can just be an overlaying popup or something similar
        }
    }

    public void enableLoginView(){
        if (this.loginView ==null){
            loginView = new LoginView();
        }
        loginView.display();
    }



    public void bindToUserView(UserView view) {
    this.view = view;
    }

    public static GUIManager getGuiManager(){
        return guiManager;
    }

    public static Stage getMainWindow() {
        return mainWindow;
    }

    public static WaitingView getWaitingView() {
        return waitingView;
    }
}
