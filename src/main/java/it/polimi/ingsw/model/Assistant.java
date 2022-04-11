package it.polimi.ingsw.model;

//I think this can be an enum, with the 1...10 assistants and that's it.
//the player can then have a map <assistant,bool> to show the asssistants

public class Assistant {
    Integer speed;
    Integer movements;

    public Assistant(Integer speed) {
        this.speed = speed;
        this.movements = (speed+1)/2;
    }


}
