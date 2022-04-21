package it.polimi.ingsw.model;
import it.polimi.ingsw.controller.PlayerController;

import java.util.*;


public class Game {
    public final int numPlayers;
    private Player currentPlayer;
    private final List<Player> tableOrder;
    private List<Player> currentOrder;
    private Integer round;
    private Map<Student, Integer> bag;
    private List<Cloud> clouds;
    private GameMap gameMap;
    public static Scanner globalScanner = new Scanner((System.in));

    /**
     * initialize bag and random order of player. round starts from 1
     */
    public Game(int numPlayers) {
        round = 1;
        this.numPlayers = numPlayers;
        bag = makeBag(); //players draw from the bag, so it's important to make this first
        clouds = makeClouds(numPlayers);
        currentOrder= startPlayersandOrder(numPlayers);
        tableOrder = new ArrayList<>(currentOrder); //this is to make a copy
        gameMap = new GameMap(this); //this will start the islands, motherNature, initial students
        fillClouds();
        currentPlayer = currentOrder.get(0);

    }


    /**
     * @return initializes the players (and their tower number), returns a random starting order.
     * This one may be moved to a playerController, or maybe from this we make the playercontroller
     * instead
     */
    private List<Player> startPlayersandOrder(int numplayers){
        ArrayList<Player> startingOrder = new ArrayList<>();
        for (int i=0; i< numplayers; i++){
            PlayerController pc = new PlayerController(i+1, this);
            startingOrder.add(pc.getPlayer());
        }
        Collections.shuffle(startingOrder);
        return startingOrder;
    }



    private HashMap<Student,Integer> makeBag(){
        HashMap<Student,Integer> bag = new HashMap<>();
        for (Student sc : Student.values()){
            bag.put(sc,24);
        }
        return bag;
    }

    /**
     *
     * @param numPlayers Make as many clouds as there are players.
     *                   They are initialized with 0 students.
     */
    private List<Cloud> makeClouds(int numPlayers){
        List<Cloud> clouds = new ArrayList<>();
        for (int i=0; i<numPlayers; i++){
            clouds.add(new Cloud(this));
        }
        return clouds;
    }

    public void newRound(){
        round += 1;
        fillClouds();

    }

    //fills all the clouds of this game by drawing from the bag
    private void fillClouds(){
        for (Cloud cloud : clouds){
            //draw from the bag
            for (int i = 0; i<cloud.size; i++) {
                Student randstud = drawFromBag();
                cloud.students.add(randstud);
            }
        }
    }

    public Student drawFromBag(){
        Random randomizer = new Random();
        int randind = randomizer.nextInt(5);
        Student randstud = Arrays.asList(Student.values()).get(randind);
        int oldnum = bag.get(randstud);
        bag.replace(randstud, oldnum, oldnum - 1);
        return randstud;
    }



    public static void main(String[] args) {
        //test for fillClouds()
        Game g = new Game(2);
        System.out.println(g.clouds);
        System.out.println(g.bag);
        g.fillClouds();
        System.out.println(g.clouds);
        System.out.println(g.bag);
    }



    //BELOW THIS ALL GETTER AND SETTERS

    public void setCurrentOrder(List<Player> currentOrder) {
        this.currentOrder = currentOrder;
    }


    public List<Player> getCurrentOrder() {
        return currentOrder;
    }

    public List<Player> getTableOrder() {
        return tableOrder;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}


