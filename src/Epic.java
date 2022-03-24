import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtask;

    public Epic(String name, String description, String status, int idTask) {
        super(name, description, status, idTask);
    }

    public Epic(String name, String description, String status, int idTask, ArrayList<Subtask> subtask) {
        super(name, description, status, idTask);
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
                "subtask=" + subtask +
                '}';
    }
}
