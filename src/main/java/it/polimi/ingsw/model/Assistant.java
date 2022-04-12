package it.polimi.ingsw.model;

public enum Assistant {
    ONE (1),
    TWO (2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10);



    private final int speed;
    private final int moves;
    Assistant(int speed) {

        this.speed = speed;
        this.moves = (speed+1)/2;
    }


    public static void main(String[] args) {
        for (Assistant a : Assistant.values())
            System.out.printf("Your assistant %s can move with a speed of %d and move mother Nature %d steps \n",
                    a, a.speed, a.moves);
    }
}
