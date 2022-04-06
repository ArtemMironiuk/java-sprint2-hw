package tasksOfDifferentTypes;

import utils.Status;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtask;

    public Epic(String name, String description, Status status, int id, ArrayList<Subtask> subtask) {
        super(name, description,status, id);
        this.subtask = subtask;
    }

    public ArrayList<Subtask> getSubtask() {
        return subtask;
    }

    public void setSubtask(ArrayList<Subtask> subtask) {
        this.subtask = subtask;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\''+
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() + '\'' +
                ", subtask=" + subtask +
                '}';
    }
}