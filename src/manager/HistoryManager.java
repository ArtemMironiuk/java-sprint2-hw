package manager;

import tasksOfDifferentTypes.Task;

import java.util.List;

public interface HistoryManager {

    /*
     * Получение списка истории просмотров.
     */
    List<Task> getHistory();

    /*
     * Добавление задачи в историю просмотров.
     */
    void add(Task task);

}
