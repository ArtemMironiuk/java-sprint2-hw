    package manager;

    import tasksOfDifferentTypes.Task;

    import java.util.List;

    /**
     * История просмотра задач.
     */
    public interface HistoryManager < T > {

        /*
         * Получение списка истории просмотров.
         */
        List<T> getHistory();

        /*
         * Добавление задачи в историю просмотров.
         */
        void add(T task);

        /**
         * Удаление задачи из просмотра.
         */
        void remove(int id);
    }
