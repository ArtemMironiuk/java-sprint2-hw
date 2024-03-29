package manager;

import exception.TasksIntersectionException;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;
import utils.Status;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int identifierTask = 1;

    protected HashMap<Integer, Task> mapTasks = new HashMap<>();
    protected HashMap<Integer, Subtask> mapSubtasks = new HashMap<>();
    protected HashMap<Integer, Epic> mapEpics = new HashMap<>();

    protected HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    protected Set<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if ((o1.getStartTime() != null) && (o2.getStartTime() != null)) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        } else if (o1.getStartTime() == null) {
            return 1;
        } else if (o2.getStartTime() == null) {
            return -1;
        }
        return 1;
    });

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(mapTasks.values());
    }

    @Override
    public void deleteTasks() {
        for (Task task : mapTasks.values()) {
            if (inMemoryHistoryManager.getHistory().contains(task)) {
                inMemoryHistoryManager.remove(task.getId());
            }
            if (prioritizedTasks.contains(task)) {
                prioritizedTasks.remove(task);
            }
        }
        mapTasks.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = mapTasks.get(id);
        inMemoryHistoryManager.add(task);//добавление задачи в историю
        return task;
    }

    @Override
    public int creatingTask(Task task) {
        if (searchForIntersections(task)) {
            task.setId(identifierTask);
            identifierTask++;
            mapTasks.put(task.getId(), task);
            prioritizedTasks.add(task);

        }
        return task.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (searchForIntersections(task)) {
            if (mapTasks.containsKey(task.getId())) {
                mapTasks.put(task.getId(), task);
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void deleteTaskId(int id) {
        if (inMemoryHistoryManager.getHistory().contains(mapTasks.get(id))) {
            inMemoryHistoryManager.remove(id);
        }
        if (prioritizedTasks.contains(mapTasks.get(id))){
            prioritizedTasks.remove(mapTasks.get(id));
        }
        mapTasks.remove(id);
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(mapSubtasks.values());
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : mapSubtasks.values()) {
            if (inMemoryHistoryManager.getHistory().contains(subtask)) {
                inMemoryHistoryManager.remove(subtask.getId());
            }
            if (prioritizedTasks.contains(subtask)) {
                prioritizedTasks.remove(subtask);
            }
            statusCalculation(subtask.getEpicId());
            localDateTimeCalculation(subtask.idEpic);

        }
        mapSubtasks.clear();
        for (Epic epic : mapEpics.values()) {
            epic.getSubtask().clear();
            statusCalculation(epic.getId());
            localDateTimeCalculation(epic.getId());
        }

    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = mapSubtasks.get(id);
        inMemoryHistoryManager.add(subtask);
        return subtask;
    }

    @Override
    public int creatingSubtask(Subtask subtask) {
        if (searchForIntersections(subtask)) {
            subtask.setId(identifierTask);
            identifierTask++;
            if (mapEpics.containsKey(subtask.idEpic)) {
                statusCalculation(subtask.getEpicId());
                localDateTimeCalculation(subtask.idEpic);
                Epic epic = mapEpics.get(subtask.idEpic);
                if (epic.getSubtask() != null) {
                    epic.getSubtask().add(subtask);
                }
            }
            mapSubtasks.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            return subtask.getId();
        } else {
            throw new TasksIntersectionException("У добавляемой задачи неверно указано время старта, есть пересечение. " + subtask);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (searchForIntersections(subtask)) {
            if (mapSubtasks.containsKey(subtask.getId())) {
                mapSubtasks.put(subtask.getId(), subtask);
                prioritizedTasks.add(subtask);
                subtask.setEpicId(subtask.idEpic);
                if (mapEpics.containsKey(subtask.idEpic)) {
                    statusCalculation(subtask.getEpicId());
                    localDateTimeCalculation(subtask.idEpic);
                    Epic epic = mapEpics.get(subtask.idEpic);
                    List<Subtask> listSubtask = epic.getSubtask();
                    listSubtask.add(subtask);
                }
            }
        }
    }

    @Override
    public void deleteSubtaskId(int id) {
        if (mapSubtasks.get(id).getEpicId() > 0) {
            int idEpic = mapSubtasks.get(id).getEpicId();
            if (inMemoryHistoryManager.getHistory().contains(mapSubtasks.get(id))) {
                inMemoryHistoryManager.remove(id);
            }
            if (prioritizedTasks.contains(mapSubtasks.get(id))) {
                prioritizedTasks.remove(mapSubtasks.get(id));
            }
            mapSubtasks.remove(id);
            if (mapEpics.containsKey(idEpic)) {
                Epic epic = mapEpics.get(idEpic);
                statusCalculation(idEpic);
                epic.getSubtask().remove(mapSubtasks.get(id));
                localDateTimeCalculation(idEpic);
            }
        }
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(mapEpics.values());
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : mapEpics.values()) {
            if (inMemoryHistoryManager.getHistory().contains(epic)) {
                inMemoryHistoryManager.remove(epic.getId());
            }
        }
        mapEpics.clear();
        for (Subtask subtask : mapSubtasks.values()) {
            if (inMemoryHistoryManager.getHistory().contains(subtask)) {
                inMemoryHistoryManager.remove(subtask.getId());
            }
            if (prioritizedTasks.contains(subtask)) {
                prioritizedTasks.remove(subtask);
            }
        }
        mapSubtasks.clear();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = mapEpics.get(id);
        inMemoryHistoryManager.add(epic);
        return epic;
    }

    @Override
    public int creatingEpic(Epic epic) {
        epic.setId(identifierTask);
        identifierTask++;
        mapEpics.put(epic.getId(), epic);
        ArrayList<Subtask> subtask = epic.getSubtask();
        if (subtask != null) {
            for (Subtask value : subtask) {
                value.setEpicId(epic.getId());
            }
        }
        statusCalculation(epic.getId());
        localDateTimeCalculation(epic.getId());
        return epic.getId();
    }

    @Override
    public void updateEpic(Epic epic) {
        if (mapEpics.containsKey(epic.getId())) {
            Epic newEpic = mapEpics.get(epic.getId());
            newEpic.setName(epic.getName());
            newEpic.setDescription(epic.getDescription());
            newEpic.setStatus(epic.getStatus());
            newEpic.setId(epic.getId());
            newEpic.setSubtask(epic.getSubtask());
            ArrayList<Subtask> subtask = epic.getSubtask();
            for (Subtask value : subtask) {
                value.setEpicId(epic.getId());
            }
            statusCalculation(newEpic.getId());
            localDateTimeCalculation(newEpic.getId());
        }
    }

    @Override
    public void deleteEpicId(int id) {
        Epic newEpic = mapEpics.get(id);
        for (Subtask subtask : newEpic.getSubtask()) {
            if (inMemoryHistoryManager.getHistory().contains(subtask)) {
                inMemoryHistoryManager.remove(subtask.getId());
            }
            mapSubtasks.remove(subtask.getId());
        }
        if (inMemoryHistoryManager.getHistory().contains(mapEpics.get(id))) {
            inMemoryHistoryManager.remove(id);
        }
        mapEpics.remove(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasksEpic(Epic epic) {
        Epic newEpic = mapEpics.get(epic.getId());
        return newEpic.getSubtask();
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);

    }

    /**
     * Установка статуса Epic по статусам Subtask.
     */
    private void statusCalculation(int idEpic) {
        int newCounter = 0;
        int doneCounter = 0;
        ArrayList<Subtask> list;
        if (mapEpics.get(idEpic).getSubtask() == null){
            mapEpics.get(idEpic).setStatus(Status.NEW);
        }
        list = mapEpics.get(idEpic).getSubtask();
        if (list == null) {
            mapEpics.get(idEpic).setStatus(Status.NEW);
        } else {
            for (Subtask value : list) {
                if (value.getStatus().equals(Status.NEW)) {
                    newCounter++;
                } else if (value.getStatus().equals(Status.DONE)) {
                    doneCounter++;
                }
            }
            if (newCounter == list.size()) {
                mapEpics.get(idEpic).setStatus(Status.NEW);
            } else if (doneCounter == list.size()) {
                mapEpics.get(idEpic).setStatus(Status.DONE);

            } else {
                mapEpics.get(idEpic).setStatus(Status.IN_PROGRES);
            }
        }
    }

    /**
     * Рассчет начала, продолжительности и конца Epic
     * @param id
     */
    private void localDateTimeCalculation(int id){
        Epic epic = mapEpics.get(id);
        epic.calculateStartTime();
        epic.calculateDuration();
        epic.calculateEndTime();
    }

    /**
     * Поиск пересечений
     */
    private boolean searchForIntersections(Task task) {
        if ((task.getStartTime() != null) && (task.getDuration() != null)) {
            List<Task> tasks = getPrioritizedTasks();
            if (tasks.isEmpty()){
                return true;
            }
            Task getTask = tasks.get(0);
            if (getTask.getStartTime() != null && getTask.getDuration() != null){
                if (task.getEndTime().isBefore(getTask.getStartTime())) {
                    return true;
                }
            } else {
                return true;
            }
            for (int i = 0; i < tasks.size() - 1; i++) {
                if (tasks.get(i).getStartTime() != null && tasks.get(i).getDuration() != null){
                    if (task.getStartTime().isAfter(tasks.get(i).getEndTime())) {
                        if (tasks.get(i + 1).getStartTime() != null && tasks.get(i + 1).getDuration() != null){
                            if (task.getEndTime().isBefore(tasks.get(i + 1).getStartTime())) {
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                }
            }
            return false;
        } else {
            return true;
        }
    }

}