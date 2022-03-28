package tasksOfDifferentTypes;

public class Subtask extends Task {
    public int idEpic;

    public Subtask(String name, String description, String status, int id, int idEpic) {
        super(name, description, status,id);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, String status, int id) {
        super(name, description, status,id);
    }

    public int getEpicId() {
        return idEpic;
    }

    public void setEpicId(int idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\''+
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() + '\'' +
                ", \nidEpic=" + idEpic +
                '}';
    }
}