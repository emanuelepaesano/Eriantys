package it.polimi.ingsw.model;
import it.polimi.ingsw.model.characters.Character;

import java.io.Serializable;
import java.util.*;


public class Game implements Serializable {
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[92m";
    public static final String ANSI_BADGREEN = "\u001B[32m";
    public static final String ANSI_PINK = "\u001B[95m";
    public static final String ANSI_YELLOW = "\u001B[93m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    private List<Player> winner;
    public final int numPlayers;
    private Player currentPlayer;
    private final List<Player> tableOrder;
    private List<Player> currentOrder;
    private Integer round;
    private Map<Student, Integer> bag;
    private List<List<Student>> clouds;
    private GameMap gameMap;

    private List<Character> characters;

    Random randomizer = new Random();
    private Boolean Over = false;

    public static Game makeGame(int numPlayers){
        List<Player> tableOrder = startPlayersandOrder(numPlayers);
        List<List<Student>> clouds = makeClouds(numPlayers);
        Map<Student, Integer> bag =  makeBag();
        GameMap gm = new GameMap(); //this will start the islands
        return new Game(numPlayers, tableOrder, clouds, bag, gm);
    }


    /**
     * @return initializes the players (and their tower number), returns a random starting order.
     * This one may be moved to a playerController, or maybe from this we make the playercontroller
     * instead
     */
    private static List<Player> startPlayersandOrder(int numPlayers){
        ArrayList<Player> startingOrder = new ArrayList<>();
        for (int i=0; i< numPlayers; i++){
            Player player = Player.makePlayer(i+1,numPlayers);
            startingOrder.add(player);
        }
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
        Map<Student,Integer> bag = new EnumMap<>(Student.class);
        for (Student sc : Student.values()){
            bag.put(sc,24);
        }
        return bag;
    }

    private Game(int numPlayers, List<Player> tableOrder, List<List<Student>> clouds,
                 Map<Student, Integer> bag, GameMap gm) {

        this.numPlayers = numPlayers;
        this.clouds = clouds;
        this.tableOrder = tableOrder;
        this.bag = bag;
        this.gameMap = gm;
        currentOrder = new ArrayList<>(this.tableOrder); //this is to make a copy
        Collections.shuffle(currentOrder);
    }


    public void doSetUp(Boolean ad){
        round = 1;
        currentPlayer = currentOrder.get(0);
        fillClouds();
        fillAllEntrancesBag();
        gameMap.startMNAndStudents();
        if (ad){
            characters = makeAllCharacters(this);
            tableOrder.forEach(p->p.setCoins(1));
        }
    }

    private List<Character> makeAllCharacters(Game game){

        characters = new ArrayList<>();
//        List<Integer> availables = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
//        for (int i=0; i<3;i++) {
//            Integer pickedChara;
//            while(true) {
//                pickedChara = 1 + randomizer.nextInt(Collections.max(availables));
//                if (availables.contains(pickedChara)) {
//                    availables.remove(pickedChara);
//                    break;
//                }
//            }
            characters.add(Character.makeCharacter(3, this));
//        }
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
        int randind = randomizer.nextInt(5);
        Student randstud = Arrays.asList(Student.values()).get(randind);
        int oldnum = bag.get(randstud);
        bag.replace(randstud, oldnum, oldnum - 1);
        return randstud;
    }



    public Boolean checkGameEndCondition(String condition, Player player){
        switch (condition) {
            case "towerend" -> {
                if (player.getNumTowers() == 0) {
                    Over = true;
                    winner = List.of(player);
                    return true;
                }
            }
            case "islandend" -> {
                if (gameMap.getArchipelago().size() <= 3) {
                    Over = true;
                    winner = lookForWinner();
                    return true;
                }
            }
            case "studend" -> {
                if (bag.equals(Map.of(Student.RED, 0, Student.BLUE, 0, Student.YELLOW, 0, Student.PINK, 0, Student.GREEN, 0))){
                Over = true;
                return true;
                }
            }
            case "deckend" -> {
                if (player.getAssistants().values().equals(List.of(false, false, false, false, false,
                        false, false, false, false, false))){
                Over = true;
                return true;}
            }
            default -> throw new RuntimeException("not a valid string as argument");
        }
        return false;
    }

    /**
     *
     * @return Looks for a winner according to the rules in case of island end, student end or deck end.<br>
     * There might be a tie with more than 1 winner
     */
    public List<Player> lookForWinner(){
        List<Player> ties = new ArrayList<>();
        List<Player> secondTies = new ArrayList<>();
        List<Integer> numtowers = tableOrder.stream().map(Player::getNumTowers).toList();
        for (Player player: tableOrder){
            if (player.getNumTowers() == Collections.max(numtowers)){
                ties.add(player);
            }
        }
        if (ties.size()==1){
            return ties;
        }
        else if (ties.size()>1){
            List<Integer> numProfessors = tableOrder.stream().map(p->Collections.frequency(p.getDiningRoom().getProfessors().values(),true)).toList();
            for (Player p:ties){
                if (Collections.frequency(p.getDiningRoom().getProfessors().values(),true)==Collections.max(numProfessors)){
                    secondTies.add(p);
                }
            }
        }
        return secondTies;
    }

    public void newRoundOrEnd(){
        Player anyPlayerisFine = getTableOrder().get(0);
        Boolean cond1 = checkGameEndCondition("deckend",anyPlayerisFine);
        Boolean cond2 = checkGameEndCondition("studend",anyPlayerisFine);
        if (cond1){
            winner = lookForWinner();
            return;
        }
        else if (cond2){
            winner= lookForWinner();
            return;
        }
        round +=1;
        fillClouds();
    }


    public static void main(String[] args) {
        //small test for wait and notify
//        Game game = Game.makeGame(3);
//        game.doSetUp(true);
//        for (Character c : game.characters)
//        {
//            System.out.println(c);
//        }
//        Character chara = game.characters.get(0);
//        game.getCurrentPlayer().setCoins(3);
//        Player user = game.currentPlayer;
//        System.out.println("current player: "+ game.getCurrentPlayer());
//        Character.play(chara,game, user);
//        game.gameMap.getArchipelago().get(0).checkOwner(game.getTableOrder());
//        synchronized (game.characters.get(0)) {
//            game.setCurrentPlayer(game.getTableOrder().get(1));
//            System.out.println("current player: "+ game.getCurrentPlayer());
//            game.characters.get(0).notifyAll();
//        }
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

    public List<Character> getCharacters() {
        return characters;
    }

    public Boolean isOver() {
        return Over;
    }

    public void setWinner(List<Player> winner) {
        this.winner = winner;
    }

    public void setClouds(List<List<Student>> clouds) {
        this.clouds = clouds;
    }

    public List<Player> getWinner() {
        return winner;
    }

    public void setOver(Boolean over) {
        Over = over;
    }
}


