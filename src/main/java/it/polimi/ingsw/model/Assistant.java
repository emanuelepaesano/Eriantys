package it.polimi.ingsw.model;

public class Assistant {
    Integer speed;
    Integer movements;

    public Assistant(Integer speed) {
        this.speed = speed;
        this.movements = (speed+1)/2;
    }


}
