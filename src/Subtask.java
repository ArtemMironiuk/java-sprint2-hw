public class Subtask extends Task {
    int idEpic;

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
                ", idEpic=" + idEpic +
                '}';
    }
}