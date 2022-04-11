package it.polimi.ingsw.model;

public enum TowerColor {
    WHITE,
    BLACK,
    GREY;


    String asString(){
        String str = "";
        switch (this){
            case WHITE:
                str = "WHITE";
                break;

            case GREY:
                str =  "GREY";
                break;
            case BLACK:
                str = "BLACK";
                break;
        }
    return str;
    }

}
