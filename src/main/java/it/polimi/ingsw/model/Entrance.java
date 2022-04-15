package it.polimi.ingsw.model;

import javax.swing.text.html.HTMLDocument;
import java.util.*;

public class Entrance {
    // TODO: 12/04/2022 missing reference to the player.
    //  actually we may not need it
    private final List<StudColor> students;
    private final DiningRoom diningRoom;

    public Entrance(int numplayers, DiningRoom diningRoom){
        int size = (numplayers == 3 ? 9 : 7);
        this.diningRoom = diningRoom;
        //initialize all entries to null
        students = new ArrayList<>(Arrays.asList(new StudColor[size]));

        //test, bad, remove
        // we will need to fill the entrance following the rules
        fillRandomTEST();
    }

    /**
     *
     * @param availablemoves the moves that are left to the player, will come from the controller.
     * First try of method for moving students to dining room. It needs to be simplified / broken down
     */
    public void moveToDiningRoom(int availablemoves){
        //First part: we ask how many students to move, maximum availablemoves
        int nstud = askHowMany(availablemoves);

        //Now we ask to move the students
        StudColor stud;
        for (int i = 0;i<nstud;i++){
            try{stud = askWhich(i);}
            catch (IllegalArgumentException ex) {
                System.out.println("Not a valid student color, try again");
                i-= 1;
                continue;
            }
            if (students.contains(stud)){
                students.remove(stud);
                int oldnum = diningRoom.getTables().get(stud);
                this.diningRoom.getTables().replace(stud,oldnum,oldnum+1);
            }
            else{
                System.out.println("You don't have this student in your entrance");
                i-=1;
            }
        }
    }

    public void moveToIsland(GameMap gm, int availablemoves){
        //here we do the same thing but with choosing an island index
        //in the second part. Let's not duplicate code, we need to make a separate method
        //for the first part.
        //Probably also for asking which student to move we need an independent method(done).

        //First part is the same.
        int nstud = askHowMany(availablemoves);
        Scanner scanner = new Scanner(System.in);
        StudColor stud;
        for (int i = 0;i<nstud;i++){
            try{stud = askWhich(i);}
            catch (IllegalArgumentException ex) {
                System.out.println("Not a valid student color, try again");
                i-= 1;
                continue;
            }
            if (students.contains(stud)){
                students.remove(stud);
                //qui adesso lo mandiamo in un isola
                System.out.println("To which island do you want to move it?\n" +
                        "This is the current state of the islands\n" + gm +
                        "\nIndicate the island by its number (0-11)") ;
                int index = scanner.nextInt(); //mettiamo poi un try-catch
                Island island = gm.getArchipelago().get(index);
                int oldval = island.students.get(stud);
                island.students.replace(stud, oldval,oldval+1);


            }
            else{
                System.out.println("You don't have this student in your entrance");
                i-=1;
            }
        }

        //in the second part we still ask which, but now we move it to one of the islands.


    }

    public int askHowMany(int availablemoves) {
        Scanner scanner = new Scanner(System.in);
        int nstud;
        while (true) {
            System.out.println("How many students do you want to move? (maximum " + availablemoves+ ")\n" +
                    "To return to action selection, type '0' or 'back' (doesn't work yet)"); //obviously back doesnt work
            String in = scanner.next();
            if (Objects.equals(in, "back")) {
                return 0;
                //Basically it will be a return null; statement, to go back to the caller (doActions)
            }
            try {
                nstud = Integer.parseInt(in);
                if (nstud > availablemoves || nstud<0){System.out.println("nice try :)");}
                else{break;}
            } catch (IllegalArgumentException ex) { System.out.println("not a number, try again");}
        }
        return nstud;
    }

    public StudColor askWhich(int number){
        Scanner scanner = new Scanner(System.in);
        System.out.println("choose the color of student number " + (number+1) + " from your entrance:" + students);
        String color = scanner.next();
        return StudColor.valueOf(color.toUpperCase());
    }

    private void fillRandomTEST(){
        Random randomizer = new Random();
        for (int i = 0; i< students.size();i++){
            int ind = randomizer.nextInt(5);
            StudColor s = Arrays.asList(StudColor.values()).get(ind);
            students.set(i,s);
        }
    }
    @Override
    public String toString() {
        return "Entrance{" +
                "students: " + students +
                '}';
    }

    public List<StudColor> getStudents() {
        return students;
    }

    public static void main(String[] args) {
        //TEST FOR MOVETODR, ALSO USES THE FILLRANDOMTEST
        GameMap gm = new GameMap();
        DiningRoom dg = new DiningRoom();
        Entrance e = new Entrance(3,dg);
        System.out.println("Chosen movetodiningroom");
        e.moveToDiningRoom(4); //choose 0 or back to test the other method
        System.out.println("Your table configuration after the moves: " + dg.getTables());
        System.out.println("Chosen movetoisland");
        e.moveToIsland(gm,4);
        System.out.println("New archipelago: " + gm);
    }


    public void moveToDiningRoomOLD(int availablemoves){
        //First part: we ask how many students to move, maximum availablemoves
        int nstud = askHowMany(availablemoves);
        //Now we ask to move the students
        Scanner scanner = new Scanner(System.in);
        for (int i = 0;i<nstud;i++){
            try {
                System.out.println("choose the color of student number " + (i+1) + " from your entrance:" + students);
                String color = scanner.next();
                StudColor stud = StudColor.valueOf(color.toUpperCase());
                if (students.contains(stud)){
                    students.remove(stud);
                    int old = diningRoom.getTables().get(stud);
                    this.diningRoom.getTables().replace(stud,old,old+1);
                }
                else{
                    System.out.println("You don't have this student in your entrance");
                    i-=1;
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Not a valid student color, try again");
                i-=1;
            }

        }
    }
}
