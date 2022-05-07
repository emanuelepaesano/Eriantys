package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.LoginMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StringMessage;

import java.util.Scanner;

public class LoginView implements View {
    String content;

    String reply;
    Scanner scanner = new Scanner(System.in);

    NetworkHandler nh;

    //static buttons and stuff


    public LoginView(NetworkHandler nh) {
        this.nh = nh;
    }

    //accessed only by the user view with update()
    @Override
    public void display() {
        //this.setVisible(true);
    }

    @Override
    public void sendReply() {
        //add graphics + a text field with a scanner
        //this should have a reference to the sh and call sendmessage on it
        reply = scanner.nextLine();
        nh.sendMessage(new LoginMessage(reply));
    }

    @Override
    public void fillInfo(Message message) {
        //fill buttons/other component with content field info.
        content = (message).toString();
    }
}
