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

    /**
     * initialize bag and random order of player. round starts from 1
     */
    public Game(int numPlayers) {
        round = 1;
        this.numPlayers = numPlayers;
        bag = makeBag();
        clouds = makeClouds(numPlayers);
        currentOrder= startPlayersandOrder(numPlayers);
        tableOrder = new ArrayList<>(currentOrder); //this is to make a copy
        gameMap = new GameMap(); //this will start the islands, motherNature, initial students
        fillClouds();
        currentPlayer = currentOrder.get(0);
        fillAllEntrancesBag();

    }



    /**
     * @return initializes the players (and their tower number), returns a random starting order.
     * This one may be moved to a playerController, or maybe from this we make the playercontroller
     * instead
     */
    private List<Player> startPlayersandOrder(int numplayers){
        ArrayList<Player> startingOrder = new ArrayList<>();
        for (int i=0; i< numplayers; i++){
            PlayerController pc = new PlayerController(i+1, this.numPlayers);
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


    /**
     * Lets the player choose a cloud and fills the entrance with the students of that cloud
     */
    // I believe now this has to be moved inside the game with player as parameter
    public void fillEntranceFromClouds(Player player){
        Scanner scanner = new Scanner(System.in);
        //this must be shown to each player, so maybe make a player.askcloud()
        System.out.println("Fill your entrance from a cloud.\n " + clouds +
                "\n enter a number from 1 to " + clouds.size() + " to choose the cloud.");
        while (true) {
            try {
                int choice = scanner.nextInt();
                if (choice<= clouds.size() && choice >= 1 ){
                    Cloud cloud = clouds.get(choice-1);
                    if (cloud.students.size()>0){
                        //add those students to our entrance
                        player.getEntrance().getStudents().addAll(cloud.students);
                        cloud.students.clear();
                        break;
                    }
                    else{System.out.println("That cloud is empty! Try again.");}
                } else {System.out.printf("Not a valid number, type a number between 1 and %d", clouds.size());}
            } catch (IllegalArgumentException ex) {System.out.println("Not a number, try again.");}
        }
    }


    //fills all entrances. New way to initialize the entrances students, in here instead of entrance
    private void fillAllEntrancesBag(){
        for (Player player : tableOrder){
            fillEntranceFromBag(player);
        }
    }

    //same for this, move inside game and take player as par.
    private void fillEntranceFromBag(Player player){
        for (int i=0;i<(numPlayers==3? 9:7);i++) {
            Student randstud = this.drawFromBag();
            player.getEntrance().getStudents().set(i,randstud);
        }
    }


    /**
     *
     * @return Map of all StudColors to 0. This method is used also by the DiningRoom to initialize, hence it's static.
     */
    public static Map<Student,Integer> makeStudents(){
        HashMap<it.polimi.ingsw.model.Student, java.lang.Integer> studs = new HashMap<>();
        for (it.polimi.ingsw.model.Student sc : it.polimi.ingsw.model.Student.values()){
            studs.put(sc,0);
        }
        return studs;
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


