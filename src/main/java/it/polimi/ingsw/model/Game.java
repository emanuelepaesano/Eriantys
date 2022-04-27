package it.polimi.ingsw.model;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.character_impls.Characters;

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

    private List<Character> characters;

    public static Game makeGame(int numPlayers){
        List<Player> startingOrder = startPlayersandOrder(numPlayers);
        List<List<Student>> clouds = makeClouds(numPlayers);
        Map<Student, Integer> bag =  makeBag();
        GameMap gm = new GameMap(); //this will start the islands
        return new Game(numPlayers, startingOrder, clouds, bag, gm);
    }


    /**
     * @return initializes the players (and their tower number), returns a random starting order.
     * This one may be moved to a playerController, or maybe from this we make the playercontroller
     * instead
     */
    private static List<Player> startPlayersandOrder(int numPlayers){
        ArrayList<Player> startingOrder = new ArrayList<>();
        for (int i=0; i< numPlayers; i++){
            PlayerController pc = new PlayerController(i+1, numPlayers);
            startingOrder.add(pc.getPlayer());
        }
        Collections.shuffle(startingOrder);
        return startingOrder;
    }

    private static List<List<Student>> makeClouds(int numPlayers){
        List<List<Student>> clouds = new ArrayList<>();
        for (int i=0; i<numPlayers; i++){
            clouds.add(new ArrayList<>());
        }
        return clouds;
    }

    private static Map<Student,Integer> makeBag(){
        Map<Student,Integer> bag = new HashMap<>();
        for (Student sc : Student.values()){
            bag.put(sc,24);
        }
        return bag;
    }

    /**
     *Constructor for GAME
     */
    private Game(int numPlayers, List<Player> startingOrder, List<List<Student>> clouds,
                 Map<Student, Integer> bag, GameMap gm) {

        this.numPlayers = numPlayers;
        this.clouds = clouds;
        this.currentOrder = startingOrder;
        this.bag = bag;
        this.gameMap = gm;
        tableOrder = new ArrayList<>(this.currentOrder); //this is to make a copy
    }


    public void doSetUp(Boolean ad){
        round = 1;
        currentPlayer = currentOrder.get(0);
        fillClouds();
        fillAllEntrancesBag();
        gameMap.startMNAndStudents();
        if (ad){characters = makeAllCharacters(this);}
    }

    private List<Character> makeAllCharacters(Game game){
        Random randomizer = new Random();
        List<Character> characters = new ArrayList<>();
        List<Integer> availables = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7));
//        for (int i=0; i<3;i++) {
//            Integer pickedChara;
//            while(true) {
//                pickedChara = 1 + randomizer.nextInt(Collections.max(availables));
//                if (availables.contains(pickedChara)) {
//                    availables.remove(pickedChara);
//                    break;
//                }
//            }
//            characters.add(Characters.makeCharacter(pickedChara, game));
//        }
        characters.add(Characters.makeCharacter(6,game));
        return characters;
    }

    private void fillClouds(){
        for(List<Student> cloud : clouds){
            for (int i = 0; i<(numPlayers==3?4:3); i++) {
                Student randstud = drawFromBag();
                cloud.add(randstud);
            }
        }
    }


    private void fillAllEntrancesBag(){
        for (Player player : tableOrder){
            fillEntranceFromBag(player);
        }
    }

    private void fillEntranceFromBag(Player player){
        for (int i=0;i<(numPlayers==3? 9:7);i++) {
            Student randstud = this.drawFromBag();
            player.getEntrance().getStudents().set(i, randstud);
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

    public void newRound(){
        round += 1;
        fillClouds();
    }



    /**
     * Lets the player choose a cloud and fills the entrance with the students of that cloud
     */
    // TODO: 24/04/2022 I think stuff like this should go inside a controller
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
                        player.getEntrance().getStudents().addAll(cloud);
                        cloud.clear();
                        break;
                    }
                    else{System.out.println("That cloud is empty! Try again.");}
                } else {System.out.printf("Not a valid number, type a number between 1 and %d", clouds.size());}
            } catch (IllegalArgumentException ex) {System.out.println("Not a number, try again.");}
        }
    }

    //fills all entrances. New way to initialize the entrances, in here instead of entrance
    //same for this, move inside game and take player as par.


    public static void main(String[] args) {
        //small test for wait and notify
        Game game = Game.makeGame(3);
        game.doSetUp(true);
        for (Character c : game.characters)
        {
            System.out.println(c);
        }
        Character chara = game.characters.get(0);
        Characters.play(chara,game.currentPlayer,game);
        System.out.println("current player: "+ game.getCurrentPlayer());
        synchronized (game.characters.get(0)) {
            game.setCurrentPlayer(game.getTableOrder().get(1));
            System.out.println("current player: "+ game.getCurrentPlayer());
            game.characters.get(0).notifyAll();
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

    public Integer getRound() {
        return round;
    }
}


