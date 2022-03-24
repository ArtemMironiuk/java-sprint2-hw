public class Subtask extends Task {
    int idEpic;


    public Subtask(String name, String description, String status, int idTask) {
        super(name, description, status, idTask);
    }

    public Subtask(String name, String description, String status, int idTask, int idEpic) {
        super(name, description, status, idTask );
        this.idEpic = idEpic;
    }

    public int getEpicId() {
        return idEpic;
    }

    public void setEpicId(int epicId) {
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic +
                '}';
    }
}