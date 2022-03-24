import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtask;

    public Epic(String name, String description, String status, int uniqueIdentificationNumber) {
        super(name, description, status, uniqueIdentificationNumber);
    }

    public Epic(String name, String description, String status, int uniqueIdentificationNumber, ArrayList<Subtask> subtask) {
        super(name, description, status, uniqueIdentificationNumber);
        this.subtask = subtask;
    }

    public ArrayList<Subtask> getSubtask() {
        return subtask;
    }

    public void setSubtask(ArrayList<Subtask> subtask) {
        this.subtask = subtask;
    }
}
