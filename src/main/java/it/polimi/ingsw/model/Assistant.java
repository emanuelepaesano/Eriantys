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


    private final int priority;
    private final int moves;
    Assistant(int priority) {
        this.priority = priority;
        this.moves = (priority+1)/2;
    }

    public int getPriority() {
        return priority;
    }

    public int getMoves() {
        return moves;
    }

    public static void main(String[] args) {
        for (Assistant a : Assistant.values())
            System.out.printf("Your assistant %s can move with a priority of %d and move mother Nature %d steps \n",
                    a, a.priority, a.moves);
    }

}
