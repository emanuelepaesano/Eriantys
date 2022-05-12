package it.polimi.ingsw.messages;

import it.polimi.ingsw.VirtualView;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Student;

import java.util.List;
import java.util.function.Consumer;

public class PickStudMessage extends Repliable implements Message {

    List<Student> students;
    String text;

    public PickStudMessage(List<Student> students) {
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
