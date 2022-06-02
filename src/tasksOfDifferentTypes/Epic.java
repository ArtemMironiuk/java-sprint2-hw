package tasksOfDifferentTypes;

import utils.Status;
import utils.TypeTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    protected LocalDateTime endTime;

    public Epic(String name, String description, Status status, int id) {
        super(name, description,status, id);
        subtasks = new ArrayList<>();
    }
    public Epic(String name, String description, Status status, int id, ArrayList<Subtask> subtasks) {
        super(name, description,status, id);
        this.subtasks = subtasks;
    }

    public ArrayList<Subtask> getSubtask() {
        return subtasks;
    }

    public void setSubtask(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (!subtasks.isEmpty()) {
            startTime = subtasks.get(0).getStartTime();
            if (subtasks.size() > 0) {
                for (int i = 1; i < subtasks.size(); i++) {
                    if (startTime.isAfter(subtasks.get(i).getStartTime())) {
                        startTime = subtasks.get(i).getStartTime();
                    }
                }
            }
        }
        return startTime;
    }

    @Override
    public Duration getDuration() {
        Duration sumDuration = Duration.ofMinutes(0);
        if (!subtasks.isEmpty()) {
            for (Subtask value : subtasks) {
                sumDuration = sumDuration.plus(value.getDuration());
            }
        }
        return sumDuration;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (!subtasks.isEmpty()) {
            this.endTime = subtasks.get(0).getEndTime();
            if (subtasks.size() > 0) {
                for (int i = 1; i < subtasks.size(); i++) {
                    if (endTime.isBefore(subtasks.get(i).getEndTime())) {
                        endTime = subtasks.get(i).getEndTime();
                    }
                }
            }
        }
        return endTime;
    }

    public TypeTasks getType() {
        return TypeTasks.EPIC;
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
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\''+
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                ", subtask=" + subtasks +
                '}';
    }
}