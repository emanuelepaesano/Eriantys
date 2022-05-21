package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.model.Student;

import java.util.List;

public class PickStudMessage extends Repliable implements Message {

    List<Student> students;
    String text;
    String whereFrom; //possible values: entrance,movetodrchar,placeinislandchar,zeropointchar

    public PickStudMessage(List<Student> students, String whereFrom) {
        this.whereFrom = whereFrom;
        this.students = students;
        text ="Choose a student color from the available ones:\n{";
        for (Student student : students){
            text += "("+student+")";
        }
        text += "} or type \"back\" to annull.";
    }

    @Override
    public void send(VirtualView user) {
        user.update(this);
    }



    @Override
    public void send(List<VirtualView> all) {
        all.forEach(v->v.update(this));
    }

    @Override
    public String getView() {
        return "pickstudent";
    }

    @Override
    public void switchAndFillView() {
        //depending on wherefrom, this will do a completely different thing

    }

    public List<Student> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public Boolean isPing() {
        return false;
    }

    @Override
    public Boolean isRepliable() {
        return true;
    }
}
