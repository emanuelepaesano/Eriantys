package it.polimi.ingsw.controller.characters;

public abstract class Characters implements Character {

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "; current Cost =" +this.getCost();
    }

}


