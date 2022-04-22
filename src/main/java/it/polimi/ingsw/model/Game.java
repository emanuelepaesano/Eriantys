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
    private List<List<Student>> clouds;
    private GameMap gameMap;

    /**
     * initialize bag and random order of player. round starts from 1
     */
    public Game(int numPlayers) {
        round = 1;
        this.numPlayers = numPlayers;
        bag = makeBag();
        currentOrder= startPlayersandOrder(numPlayers);
        tableOrder = new ArrayList<>(currentOrder); //this is to make a copy
        gameMap = new GameMap(); //this will start the islands, motherNature, initial students
        currentPlayer = currentOrder.get(0);
        clouds = makeClouds(numPlayers);
        fillClouds();
        fillAllEntrancesBag();

    }

    private List<List<Student>> makeClouds(int numPlayers){
        List<List<Student>> clouds = new ArrayList<>();
        for (int i=0; i<numPlayers; i++){
            clouds.add(new ArrayList<>());
        }
        return clouds;
    }

    private void fillClouds(){
        for(List<Student> cloud : clouds){
            for (int i = 0; i<(numPlayers==3?4:3); i++) {
                Student randstud = drawFromBag();
                cloud.add(randstud);
            }
        }
    }



    /**
     * @return initializes the players (and their tower number), returns a random starting order.
     * This one may be moved to a playerController, or maybe from this we make the playercontroller
     * instead
     */
    private List<Player> startPlayersandOrder(int numplayers){
        ArrayList<Player> startingOrder = new ArrayList<>();
        for (int i=0; i< numplayers; i++){
            DiningRoom diningRoom = new DiningRoom();
            Entrance entrance = new Entrance(this.numPlayers);
            School school = new School(diningRoom, entrance);
            PlayerController pc = new PlayerController(i+1, this.numPlayers, school);
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


    public void newRound(){
        round += 1;
        fillClouds();
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
                    List<Student> cloud = clouds.get(choice-1);
                    if (cloud.size()>0){
                        //add those students to our entrance
                        player.getSchool().getEntrance().getStudents().addAll(cloud);
                        cloud.clear();
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
            player.getSchool().getEntrance().getStudents().set(i, randstud);
        }
    }






    public static void main(String[] args) {
        //test for fillClouds()
        Game g = new Game(3);
        for(Player p: g.getTableOrder()) {
            g.fillEntranceFromClouds(p);
        }
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

    public List<List<Student>> getClouds() {
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


