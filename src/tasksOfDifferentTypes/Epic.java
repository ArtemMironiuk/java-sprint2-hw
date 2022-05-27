package tasksOfDifferentTypes;

import utils.Status;
import utils.TypeTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, Status status, int id){
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

        LocalDateTime startTime = subtasks.get(0).getStartTime();
        if(subtasks.size() > 0) {
            for (int i = 1; i < subtasks.size(); i++) {
                if(startTime.isBefore(subtasks.get(i).getStartTime())) {
                    startTime = subtasks.get(i).getStartTime();
                }
            }
        }
        return startTime;
    }

    @Override
    public Duration getDuration() {
        Duration sumDuration = Duration.ofMinutes(0);
        for (Subtask value : subtasks) {
            sumDuration = sumDuration.plus(value.getDuration());
        }
        return sumDuration;
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime endTime = null;
        if(subtasks!= null) {
            endTime = subtasks.get(subtasks.size() - 1).getEndTime();
        }
        return endTime;
    }

    public TypeTasks getType() {
        return TypeTasks.EPIC;
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