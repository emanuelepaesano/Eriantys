package it.polimi.ingsw.CLIENT;

public class InitState implements ViewState {
    Object model;


    public InitState() {
    }


    @Override
    public void display() {
        System.out.println(model);
    }
}
