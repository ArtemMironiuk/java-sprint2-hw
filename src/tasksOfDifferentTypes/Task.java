package tasksOfDifferentTypes;

import utils.Status;
import utils.TypeTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task{

    protected String name;
    protected String description;
    protected Status status;
    protected int id;
    protected TypeTasks typeTask = TypeTasks.TASK;

    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String name, String description, Status status, int id, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, Status status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (this.startTime == null){
            return null;
        }
        if ((this.duration == null)){
            return null;
        }
        return startTime.plus(duration);
    }


    public TypeTasks getType() {
        return typeTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                status == task.status &&
                Objects.equals(startTime, task.startTime) &&
                Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, startTime, duration);
    }

    @Override
    public String toString() {
        return "\nTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}';
    }
}