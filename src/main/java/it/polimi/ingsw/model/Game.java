package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.BlockIslandCharacter;
import it.polimi.ingsw.model.characters.Character;

import java.io.Serializable;
import java.util.*;


public class Game implements Serializable {
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[92m";
    public static final String ANSI_BADGREEN = "\u001B[32m";
    public static final String ANSI_PINK = "\u001B[95m";
    public static final String ANSI_YELLOW = "\u001B[93m";
    public static final String ANSI_BLUE = "\u001B[34m";
    private List<Player> winner;
    public final int numPlayers;
    private Player currentPlayer;
    private final List<Player> tableOrder;
    private List<Player> currentOrder;
    private Integer round;
    private final Map<Student, Integer> bag;
    private final List<List<Student>> clouds;
    private final GameMap gameMap;

    private List<Character> characters;

    private final Random randomizer = new Random();
    private Boolean over = false;
    private Boolean advanced;

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

    /**
     * Make clouds based on the number of players.
     *
     * @param numPlayers
     * @return clouds
     */
    private static List<List<Student>> makeClouds(int numPlayers){
        List<List<Student>> clouds = new ArrayList<>();
        for (int i=0; i<numPlayers; i++){
            clouds.add(new ArrayList<>());
        }
        return clouds;
    }

    /**
     * Make bag.
     *
     * @return bag
     */
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


    /**
     * Set up the game.
     *
     * @param ad true if the game is expert mode
     */
    public void doSetUp(Boolean ad){
        this.advanced = ad;
        round = 1;
        currentPlayer = currentOrder.get(0);
        fillClouds();
        fillAllEntrancesFromBag();
        gameMap.startMNAndStudents();
        if (ad){
            characters = makeAllCharacters();
            tableOrder.forEach(p->p.setCoins(1));
        }
    }

    /**
     * Select three characters randomly.
     *
     * @return list of three characters selected randomly.
     */
    private List<Character> makeAllCharacters(){
        List<Character> characters = new ArrayList<>();
        List<Integer> availables = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        for (int i=0; i<3;i++) {
            Integer pickedChara;
            while(true) {
                //random 1~12
                pickedChara = 1 + randomizer.nextInt(Collections.max(availables));
                if (availables.contains(pickedChara)) {
                    availables.remove(pickedChara);
                    break;
                }
            }
            Character newCharacter = Character.makeCharacter(pickedChara,this);
            if (newCharacter.getNumber() == 12){
                gameMap.setBlockChar((BlockIslandCharacter) newCharacter);
            }
            characters.add(newCharacter);
        }
        return characters;
    }

    /**
     * Fill the clouds.
     */
    private void fillClouds(){
        clouds.forEach(List::clear);
        for(List<Student> cloud : clouds){
            for (int i = 0; i<(numPlayers==3?4:3); i++) {
                Student randstud = drawFromBag();
                cloud.add(randstud);
            }
        }
    }


    /**
     * Fill the entrance with students for all players.
     */
    private void fillAllEntrancesFromBag(){
        for (Player player : tableOrder){
            fillOneEntranceFromBag(player);
        }
    }

    /**
     * Fill the entrance with students for the player.
     *
     * @param player
     */
    private void fillOneEntranceFromBag(Player player){
        for (int i=0;i<(numPlayers==3? 9:7);i++) {
            Student randstud = this.drawFromBag();
            player.getEntrance().getStudents().set(i, randstud);
        }
    }

    /**
     * Draw students from the bag.
     *
     * @return
     */
    public Student drawFromBag(){
        int randind = randomizer.nextInt(5);
        Student randstud = Arrays.asList(Student.values()).get(randind);
        int oldnum = bag.get(randstud);
        bag.replace(randstud, oldnum, oldnum - 1);
        return randstud;
    }

    /**
     * Add students to the bag.
     *
     */
    public void addToBag(Student student, Integer num){
        int oldnum = bag.get(student);
        bag.replace(student, oldnum, oldnum + num);
    }

    /**
     * Check if the game can end.
     *
     * @param condition the end condition to check.
     * @param player
     * @return
     */
    public Boolean checkGameEndCondition(String condition, Player player){
        switch (condition) {
            case "towerend" -> {
                if (player.getNumTowers() == 0) {
                    over = true;
                    winner = List.of(player);
                    return true;
                }
            }
            case "islandend" -> {
                if (gameMap.getArchipelago().size() <= 3) {
                    over = true;
                    winner = lookForWinner();
                    return true;
                }
            }
            case "studend" -> {
                if (bag.equals(Map.of(Student.YELLOW, 0, Student.BLUE, 0, Student.RED, 0, Student.PINK, 0, Student.GREEN, 0))){
                over = true;
                return true;
                }
            }
            case "deckend" -> {
                if (!player.getAssistants().containsValue(true)){
                over = true;
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

    /**
     * Check if the game goes to the next round or end.
     */
    public void newRoundOrEnd(){
        if (this.isOver()){
            return;
        }
        Player anyPlayerisFine = getTableOrder().get(0);
        Boolean cond1 = checkGameEndCondition("deckend",anyPlayerisFine);
        Boolean cond2 = checkGameEndCondition("studend",anyPlayerisFine);
        if (cond1){
            winner = lookForWinner();
            over = true;
            return;
        }
        else if (cond2){
            winner= lookForWinner();
            over = true;
            return;
        }
        round +=1;
        fillClouds();
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

    public List<Character> getCharacters() {
        return characters;
    }

    public Boolean isOver() {
        return over;
    }

    public List<Player> getWinner() {
        return winner;
    }

    public boolean isAdvanced() {
        return advanced;
    }
}


