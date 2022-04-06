package manager;

import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;

import java.util.ArrayList;
import java.util.Collection;

public interface TaskManager {

    /**
     * Получение списка всех задач.
     */
    Collection<Task> getTasks();
    /**
     * Удаление всех задач.
     */
    void deleteTasks();
    /**
     * Получение задачи по идентификатору.
     */
    Task getTask(int id);
    /**
     * Создание.Сам объект должен передаваться в качестве параметра.
     * @param task
     */
    void creatingTask(Task task);
    /**
     * Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
     * @param task
     */
    void updateTask(Task task);
    /**
     * Удаление по идентификатору.
     */
    void deleteTaskId(int id);
    /**
     * Получение списка всех подзадач.
     */
    Collection<Subtask> getSubtasks();
    /**
     * Удаление всех подзадач.
     */
    void deleteSubtasks();
    /**
     * Получение по идентификатору.
     */
    Subtask getSubtask(int id);
    /**
     * Создание подзадачи.Сам объект должен передаваться в качестве параметра.
     * @param subtask
     */
    void creatingSubtask(Subtask subtask);
    /**
     * Обновление подзадачи. Новая версия объекта с верным идентификатором передаются в виде параметра.
     * @param subtask
     */
    void updateSubtask(Subtask subtask);
    /**
     * Удаление подзадачи по идентификатору.
     */
    void deleteSubtaskId(int id);

    /**
     * Получение списка всех Epics.
     */
    Collection<Epic> getEpics();
    /**
     * Удаление всех Epics.
     */
    void deleteEpics();
    /**
     * Получение по идентификатору.
     */
    Epic getEpic(int id);
    /**
     * Создание Epic.Сам объект должен передаваться в качестве параметра.
     * @param epic
     */
    void creatingEpic(Epic epic);
    /**
     * Обновление tasksOfDifferentTypes.Epic. Новая версия объекта с верным идентификатором передаются в виде параметра.
     * @param epic
     */
    void updateEpic(Epic epic);
    /**
     * Удаление по идентификатору.
     */
    void deleteEpicId(int id);
    /**
     * Получение списка всех подзадач определённого эпика.
     * @param epic
     */
    ArrayList<Subtask> getSubtasksEpic(Epic epic);
}
