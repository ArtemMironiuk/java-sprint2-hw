    package manager;

    import tasksOfDifferentTypes.Task;

    import java.util.List;

    /**
     * История просмотра задач.
     */
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
