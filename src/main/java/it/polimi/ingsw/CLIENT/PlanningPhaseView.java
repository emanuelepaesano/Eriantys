package it.polimi.ingsw.CLIENT;

public class PlanningPhaseView implements ViewState{
    String model;

    public PlanningPhaseView(Object args) {
        this.model = (String) args;
    }

    @Override
    public void display() {
        System.out.println(model);
    }
}
