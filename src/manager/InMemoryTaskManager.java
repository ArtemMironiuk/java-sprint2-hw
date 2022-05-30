package manager;

import exception.TasksIntersectionException;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;
import utils.Status;

import java.time.LocalDateTime;
import java.util.*;

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
    public int creatingTask(Task task) {
        if (searchForIntersections(task)) {
            task.setId(identifierTask);
            identifierTask++;
            mapTasks.put(task.getId(), task);
            return task.getId();
        } else {
            throw new TasksIntersectionException("У добавляемой задачи неверно указано время старта, есть пересечение. " + task);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (mapTasks.containsKey(task.getId())) {
            mapTasks.put(task.getId(), task);
            searchForIntersections(task);
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
    public int creatingSubtask(Subtask subtask) {
        if (searchForIntersections(subtask)) {
            subtask.setId(identifierTask);
            identifierTask++;
            mapSubtasks.put(subtask.getId(), subtask);
            if (mapEpics.containsKey(subtask.idEpic)) {
                statusCalculation(subtask.getEpicId());
            }
            return subtask.getId();
        } else {
            throw new TasksIntersectionException("У добавляемой задачи неверно указано время старта, есть пересечение. " + subtask);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (mapSubtasks.containsKey(subtask.getId())) {
            mapSubtasks.put(subtask.getId(), subtask);
            subtask.setEpicId(subtask.idEpic);
            searchForIntersections(subtask);
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
    public int creatingEpic(Epic epic) {
        epic.setId(identifierTask);
        identifierTask++;
        mapEpics.put(epic.getId(), epic);
        ArrayList<Subtask> subtask = epic.getSubtask();
        for (Subtask value : subtask) {
            value.setEpicId(epic.getId());
        }
        statusCalculation(epic.getId());
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

    @Override
    public List<Task> getPrioritizedTasks() {
        Set<Task> tasks = new TreeSet<>((o1, o2) -> {
//            if (o1.getStartTime() == null || o2.getStartTime() == null) {
//                return -1;
//            } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
//                return 1;
//
//            } else if (o1.getStartTime().isBefore(o2.getStartTime())) {
//                return -1;
//
//            } else {
//                return 0;
//            }
            if ((o1.getStartTime() != null) && (o2.getStartTime() != null)) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            } else if (o1.getStartTime() == null) {
                return 1;
            } else if (o2.getStartTime() == null) {
                return -1;
            }
            return 1;
        });

        tasks.addAll(mapTasks.values());
        tasks.addAll(mapSubtasks.values());


//        for (Map.Entry<Integer, Task> entry : mapTasks.entrySet()) {
//            tasks.add(entry.getValue());
//        }
//        for (Map.Entry<Integer, Subtask> entry : mapSubtasks.entrySet()) {
//            tasks.add(entry.getValue());
//        }

        return new ArrayList<Task>(tasks);

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

//    throw new TasksIntersectionException("Предыдущая задача не закончена " + tasks.get(i) + " . Невозможно " +
//            "начать следующую " + tasks.get(i + 1));

    /**
     * Поиск пересечений
     */
    private boolean searchForIntersections(Task task) {
        if ((task.getStartTime() != null) && (task.getDuration() != null)) {
            List<Task> tasks = getPrioritizedTasks();
            Task getTask = tasks.get(0);
            if (getTask.getStartTime() != null && getTask.getDuration() != null){
                if (task.getEndTime().isBefore(getTask.getStartTime())) {
                    return true;
                }
            }else {
                return true;
            }
            for (int i = 0; i < tasks.size() - 1; i++) {
                if (tasks.get(i).getStartTime() != null && tasks.get(i).getDuration() != null){
                    if (task.getStartTime().isAfter(tasks.get(i).getEndTime())) {
                        if (tasks.get(i + 1).getStartTime() != null && tasks.get(i + 1).getDuration() != null){
                            if (task.getEndTime().isBefore(tasks.get(i + 1).getStartTime())) {
                                return true;
                            }
                        }else {
                            return true;
                        }
                    }
                }
            }
            return false;
//            throw new TasksIntersectionException("У добавляемой задачи неверно указано время старта, есть пересечение.");
        } else {
            return true;
        }
    }
}