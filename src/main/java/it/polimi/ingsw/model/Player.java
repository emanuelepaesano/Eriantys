package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Player implements Serializable {
    public final int id ;
    private String playerName;
    private TowerColor towerColor;
    private Integer wizard;
    private final DiningRoom diningRoom;
    private final Entrance entrance;
    private Integer numTowers;
    private Map<Assistant, Boolean> assistants;
    private Assistant currentAssistant;
    private final int numActions;
    private int baseMoves = 0;
    private int baseInfluence = 0;
    private boolean orEqual = false;

    public static Player makePlayer(int id, int numPlayers){
        Map<Assistant, Boolean> assistants = buildDeck();
        DiningRoom diningRoom = new DiningRoom();
        Entrance entrance = new Entrance(numPlayers);
        return new Player(id, assistants, (numPlayers==3? 4 : 3), (numPlayers == 3? 6 : 8), diningRoom, entrance);
    }


    private static Map<Assistant, Boolean> buildDeck(){
        Map<Assistant,Boolean> tm = new TreeMap<>();
        for (Assistant as : Assistant.values()) {
            tm.put(as, true);
        }
        return tm;
    }


    private Player(int id, Map<Assistant,Boolean> deck,
                  int numActions, int numTowers, DiningRoom diningRoom, Entrance entrance) {
        this.id = id;
        assistants = deck;
        this.numActions = numActions;
        this.numTowers = numTowers;
        this.diningRoom = diningRoom;
        this.entrance = entrance;
    }


    /**
     *Method to calculate this player's influence on an island. It gets called by Island.checkOwner().
     */
    public int calculateInfluence(Island island) {
        int influence = baseInfluence;
        if (island.getOwner() == this) {influence += island.size;}
        for (Student student : Student.values()){
            if (this.diningRoom.getProfessors().get(student)){
                influence += island.getStudents().get(student);
            }
        }
        return influence;
    }


    @Override
    public String toString() {
        return "Player" + id +
                ": \"" + playerName +"\"";
    }





    //GETTERS SETTERS

    public Integer getCoins(){
        return this.getDiningRoom().getCoins();
    }
    public void setCoins(int coins){
        this.getDiningRoom().setCoins(coins);
    }
    public void setNumTowers(Integer numTowers) {
        this.numTowers = numTowers;
    }

    public Map<Assistant, Boolean> getAssistants() {
        return assistants;
    }

    public DiningRoom getDiningRoom() {
        return diningRoom;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    public Integer getNumTowers() {
        return numTowers;
    }

    public Assistant getCurrentAssistant() {
        return currentAssistant;
    }

    public Integer getWizard() {
        return wizard;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void setCurrentAssistant(Assistant currentAssistant) {
        this.currentAssistant = currentAssistant;
    }


    public void setBaseInfluence(int baseInfluence) { this.baseInfluence = baseInfluence;}

    public int getId() {
        return id;
    }

    public void setBaseMoves(int baseMoves) {
        this.baseMoves = baseMoves;
    }

    public void setOrEqual(boolean orEqual) {this.orEqual = orEqual;}

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setWizard(Integer wizard) {
        this.wizard = wizard;
    }

    public int getNumActions() {
        return numActions;
    }

    public boolean isOrEqual() {
        return orEqual;
    }

    public int getBaseMoves() {
        return baseMoves;
    }

    public int getBaseInfluence() {
        return baseInfluence;
    }


}
