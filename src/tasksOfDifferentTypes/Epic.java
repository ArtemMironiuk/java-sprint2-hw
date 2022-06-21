package tasksOfDifferentTypes;

import utils.Status;
import utils.TypeTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Subtask> subtasks;
    protected LocalDateTime endTime;

    public Epic(String name, String description, Status status, int id) {
        super(name, description,status, id);
        this.subtasks = new ArrayList<>();
        typeTask = TypeTasks.EPIC;
    }
    public Epic(String name, String description, Status status, int id, ArrayList<Subtask> subtasks) {
        super(name, description,status, id);
        this.subtasks = subtasks;
        typeTask = TypeTasks.EPIC;
    }

    public Epic(){
        this.subtasks = new ArrayList<>();
        typeTask = TypeTasks.EPIC;
    }

    public ArrayList<Subtask> getSubtask() {
        return subtasks;
    }

    public void setSubtask(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }
    /**
     * Рассчет времени начала Epic
     */
    public void calculateStartTime() {
        if (subtasks != null) {
            if (subtasks.size()>0) {
                startTime = subtasks.get(0).getStartTime();
                if (subtasks.size() > 0) {
                    for (int i = 1; i < subtasks.size(); i++) {
                        if (startTime.isAfter(subtasks.get(i).getStartTime())) {
                            startTime = subtasks.get(i).getStartTime();
                        }
                    }
                }
            }
        }
    }
    /**
     * Рассчет продолжительности Epic
     */
    public void calculateDuration() {
        duration = Duration.ofMinutes(0);
        if (subtasks != null) {
            for (Subtask value : subtasks) {
                duration = duration.plus(value.getDuration());
            }
        }
    }
    /**
     * Рассчет времени окончания Epic
     */
    public void calculateEndTime() {
        if (subtasks != null) {
            if(subtasks.size() > 0) {
                this.endTime = subtasks.get(0).getEndTime();
                if (subtasks.size() > 0) {
                    for (int i = 1; i < subtasks.size(); i++) {
                        if (endTime.isBefore(subtasks.get(i).getEndTime())) {
                            endTime = subtasks.get(i).getEndTime();
                        }
                    }
                }
            }
        }
    }

    public TypeTasks getType() {
        return typeTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return
                Objects.equals(subtasks, epic.subtasks) &&
                        Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks, endTime);
    }

    @Override
    public String toString() {
        return "Epic{" +
                " name='" + getName() + '\'' +
                ", description='" + getDescription() + '\''+
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                ", endTime='" + endTime + '\'' +
                ", subtask=" + getSubtask() +
                '}';
    }
}