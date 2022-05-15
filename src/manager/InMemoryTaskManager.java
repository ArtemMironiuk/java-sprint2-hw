package manager;

import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;
import utils.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    protected int identifierTask = 0;

    protected final HashMap<Integer, Task> mapTasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> mapSubtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> mapEpics = new HashMap<>();

    protected final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();


    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(mapTasks.values());
    }

    @Override
    public void deleteTasks() {
        for (Task task : mapTasks.values()) {
            inMemoryHistoryManager.remove(task.getId());
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
    public void creatingTask(Task task) {
//    public int creatingTask(Task task) {
        task.setId(identifierTask);
        identifierTask++;
        mapTasks.put(task.getId(), task);
//        return task.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (mapTasks.containsKey(task.getId())) {
            mapTasks.put(task.getId(), task);
        }
    }

    @Override
    public void deleteTaskId(int id) {
        mapTasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(mapSubtasks.values());
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : mapSubtasks.values()) {
            inMemoryHistoryManager.remove(subtask.getId());
            statusCalculation(subtask.getEpicId());
        }
        mapSubtasks.clear();
        for (Epic epic : mapEpics.values()) {
            epic.getSubtask().clear();
            statusCalculation(epic.getId());
        }

    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = mapSubtasks.get(id);
        inMemoryHistoryManager.add(subtask);
        return subtask;
    }

    @Override
    public void creatingSubtask(Subtask subtask) {
        subtask.setId(identifierTask);
        identifierTask++;
        mapSubtasks.put(subtask.getId(), subtask);
        if (mapEpics.containsKey(subtask.idEpic)) {
            statusCalculation(subtask.getEpicId());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (mapSubtasks.containsKey(subtask.getId())) {
            mapSubtasks.put(subtask.getId(), subtask);
            subtask.setEpicId(subtask.idEpic);
            if (mapEpics.containsKey(subtask.idEpic)) {
                statusCalculation(subtask.getEpicId());
            }
        }
    }

    @Override
    public void deleteSubtaskId(int id) {
        if (mapSubtasks.get(id).getEpicId() >= 0) {
            int idEpic = mapSubtasks.get(id).getEpicId();
            if (mapEpics.containsKey(idEpic)) {
                statusCalculation(idEpic);
            }
        }
        mapSubtasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(mapEpics.values());
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : mapEpics.values()) {
            inMemoryHistoryManager.remove(epic.getId());
        }
        mapEpics.clear();
        for (Subtask subtask : mapSubtasks.values()) {
            inMemoryHistoryManager.remove(subtask.getId());
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
    public void creatingEpic(Epic epic) {
        epic.setId(identifierTask);
        identifierTask++;
        mapEpics.put(epic.getId(), epic);
        ArrayList<Subtask> subtask = epic.getSubtask();
        for (Subtask value : subtask) {
            value.setEpicId(epic.getId());
        }
        statusCalculation(epic.getId());
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
        }
    }

    @Override
    public void deleteEpicId(int id) {
        Epic newEpic = mapEpics.remove(id);
        for (Subtask subtask : newEpic.getSubtask()) {
            mapSubtasks.remove(subtask.getId());
            inMemoryHistoryManager.remove(subtask.getId());
        }
        mapEpics.remove(id);
        inMemoryHistoryManager.remove(id);
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

    /**
     * Установка статуса Epic по статусам Subtask.
     */
    private void statusCalculation(int idEpic) {
        int newCounter = 0;
        int doneCounter = 0;
        ArrayList<Subtask> list;
        list = mapEpics.get(idEpic).getSubtask();
        if (list.isEmpty()) {
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
}
