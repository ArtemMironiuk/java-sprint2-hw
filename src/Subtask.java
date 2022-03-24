public class Subtask extends Task {
    int idEpic;


    public Subtask(String name, String description, String status, int uniqueIdentificationNumber) {
        super(name, description, status, uniqueIdentificationNumber);
    }

    public Subtask(String name, String description, String status, int uniqueIdentificationNumber, int idEpic) {
        super(name, description, status, uniqueIdentificationNumber);
        this.idEpic = idEpic;
    }

    public int getEpicId() {
        return idEpic;
    }

    public void setEpicId(int epicId) {
        this.idEpic = idEpic;
    }
}