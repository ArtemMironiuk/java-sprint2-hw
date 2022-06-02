package tasksOfDifferentTypes;

import utils.Status;
import utils.TypeTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    public int idEpic;

    public Subtask(String name, String description, Status status, int id,int idEpic, LocalDateTime startTime, Duration duration) {
        super(name, description, status, id, startTime, duration);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, Status status, int id, int idEpic) {
        super(name, description, status,id);
        this.idEpic = idEpic;
    }

    public int getEpicId() {
        return idEpic;
    }

    public void setEpicId(int idEpic) {
        this.idEpic = idEpic;
    }

    public TypeTasks getType() {
        return TypeTasks.SUBTASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
            Subtask subtask = (Subtask) o;
            return idEpic == subtask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),idEpic);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\''+
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() + '\'' +
                ", \nidEpic=" + idEpic + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                ", endTime='" + getEndTime() +
                '}';
    }
}