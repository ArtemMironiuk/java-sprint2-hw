import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Manager {
    int identifierTask = 0;

    private HashMap<Integer, Task> mapTasks = new HashMap<>();
    private HashMap<Integer, Subtask> mapSubtasks = new HashMap<>();
    private HashMap<Integer, Epic> mapEpics = new HashMap<>();

    /**
     * Получение списка всех задач.
     * @return
     */
    public Collection<Task> getTasks() {
        return mapTasks.values();
    }
    /**
     * Удаление всех задач.
     */
    public void deleteTasks() {
        mapTasks.clear();
    }
    /**
     * Получение задачи по идентификатору.
     */
    public Task getTask(int id) {
        return mapTasks.get(id);
    }
    /**
     * Создание.Сам объект должен передаваться в качестве параметра.
     * @param task
     */
    public void creatingTask(Task task) {
        task.setId(identifierTask);
        identifierTask++;
        mapTasks.put(task.getId(), task);
    }
    /**
     * Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
     * @param task
     */
    public void updateTask(Task task) {
        if (mapTasks.containsKey(task.getId())) {
            mapTasks.put(task.getId(), task);
        } else {
            task.setId(identifierTask);
            identifierTask++;
        }
    }
    /**
     * Удаление по идентификатору.
     */
    public void deleteTaskId(int id) {
        mapTasks.remove(id);
    }
    /**
     * Получение списка всех подзадач.
     * @return
     */
    public Collection<Subtask> getSubtasks() {
        return mapSubtasks.values();
    }
    /**
     * Удаление всех подзадач.
     */
    public void deleteSubtasks() {
        mapSubtasks.clear();
    }
    /**
     * Получение по идентификатору.
     */
    public Subtask getSubtask(int id) {
        return mapSubtasks.get(id);
    }
    /**
     * Создание подзадачи.Сам объект должен передаваться в качестве параметра.
     * @param subtask
     */
    public void creatingSubtask(Subtask subtask) {
        subtask.setId(identifierTask);
        identifierTask++;
        mapSubtasks.put(subtask.getId(), subtask);
        subtask.setEpicId(subtask.idEpic);
    }
    /**
     * Обновление подзадачи. Новая версия объекта с верным идентификатором передаются в виде параметра.
     * @param subtask
     */
    public void updateSubtask(Subtask subtask) {
        if (mapSubtasks.containsKey(subtask.getId())) {
            mapSubtasks.put(subtask.getId(), subtask);
            subtask.setEpicId(subtask.idEpic);
        }
        subtask.setId(identifierTask);
        identifierTask++;
    }
    /**
     * Удаление подзадачи по идентификатору.
     */
    public void deleteSubtaskId(int id) {
        mapSubtasks.remove(id);
    }
    /**
     * Получение списка всех Epics.
     */
    public Collection<Epic> getEpics() {
        return mapEpics.values();
    }
    /**
     * Удаление всех Epics.
     */
    public void deleteEpics() {
        mapEpics.clear();
        mapSubtasks.clear();
    }
    /**
     * Получение по идентификатору.
     */
    public Epic getEpic(int id) {
        return mapEpics.get(id);
    }
    /**
     * Создание Epic.Сам объект должен передаваться в качестве параметра.
     * @param epic
     * @return
     */
    public void creatingEpic(Epic epic) {
        epic.setId(identifierTask);
        identifierTask++;
        mapEpics.put(epic.getId(), epic);
        setEpicStatus();
    }
    /**
     * Обновление Epic. Новая версия объекта с верным идентификатором передаются в виде параметра.
     * @param epic
     */
    public void updateEpic(Epic epic) {
        if (mapEpics.containsKey(epic.getId())) {
            Epic newEpic = mapEpics.get(epic.getId());
            newEpic.setName(epic.getName());
            newEpic.setDescription(epic.getDescription());
            newEpic.setStatus(epic.getStatus());
            newEpic.setId(epic.getId());
            newEpic.setSubtask(epic.getSubtask());
            setEpicStatus();
        } else {
            epic.setId(identifierTask);
            identifierTask++;
        }
    }
    /**
     * Удаление по идентификатору.
     */
    public void deleteEpicId(int id) {
        Epic newEpic = mapEpics.remove(id);
        for (Subtask subtask : newEpic.getSubtask()) {
            mapSubtasks.remove(subtask.getId());
        }
        mapEpics.remove(id);
    }
    /**
     * Получение списка всех подзадач определённого эпика.
     * @param epic
     * @return
     */
    public ArrayList<Subtask> getSubtasksEpic(Epic epic) {
        Epic newEpic = mapEpics.get(epic.getId());
        return newEpic.getSubtask();
    }
    /**
     * Установка статуса Epic по статусам Subtask.
     */
    private void setEpicStatus() {
        int newCounter = 0;
        int doneCounter = 0;
        for (Epic epics : mapEpics.values()) {
            ArrayList<Subtask> subtask = epics.getSubtask();
            if (subtask.size() == 0) {
                epics.setStatus(Status.NEW);
            } else {
                for (int i = 0; i < subtask.size(); i++) {
                    if (subtask.get(i).getStatus().equals(Status.NEW)) {
                        newCounter++;
                    } else if (subtask.get(i).getStatus().equals(Status.DONE)) {
                        doneCounter++;
                    }
                }
                if (newCounter == subtask.size()) {
                    epics.setStatus(Status.NEW);
                } else if (doneCounter == subtask.size()) {
                    epics.setStatus(Status.DONE);
                } else {
                    epics.setStatus(Status.IN_PROGRES);
                }
            }
        }
    }
}
