package it.polimi.ingsw.CLIENT.ViewImpls;

import it.polimi.ingsw.CLIENT.NetworkHandler;
import it.polimi.ingsw.CLIENT.View;
import it.polimi.ingsw.messages.Message;

import java.util.Scanner;

public class CLIView implements View {

    String content;
    String reply;
    Scanner scanner = new Scanner(System.in);
    NetworkHandler nh;

    public CLIView(NetworkHandler nh) {
        this.nh = nh;
        Thread t = new Thread(this::speakerThreadTask);
        t.start();
    }

    @Override
    public void display() {
        System.out.println(content);
    }

    public void sendReply() {
        reply = scanner.nextLine();
        nh.sendReply(reply);
    }

    private void speakerThreadTask(){
        while(true){
            sendReply();
        }
    }

    @Override
    public void fillInfo(Message message) {
        content = message.toString();
    }
}
