    package manager;

    import tasksOfDifferentTypes.Epic;
    import tasksOfDifferentTypes.Subtask;
    import tasksOfDifferentTypes.Task;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;

    /**
     * Менеджер задач.
     */
    public interface TaskManager {

        /**
         * Получение списка всех задач.
         */
        List<Task> getTasks() throws IOException, InterruptedException;
        /**
         * Удаление всех задач.
         */
        void deleteTasks() throws IOException, InterruptedException;
        /**
         * Получение задачи по идентификатору.
         */
        Task getTask(int id) throws IOException, InterruptedException;
        /**
         * Создание.Сам объект должен передаваться в качестве параметра.
         * @param task
         */
        int creatingTask(Task task) throws IOException, InterruptedException;
        /**
         * Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
         * @param task
         */
        void updateTask(Task task) throws IOException, InterruptedException;
        /**
         * Удаление по идентификатору.
         */
        void deleteTaskId(int id) throws IOException, InterruptedException;
        /**
         * Получение списка всех подзадач.
         */
        List<Subtask> getSubtasks() throws IOException, InterruptedException;
        /**
         * Удаление всех подзадач.
         */
        void deleteSubtasks() throws IOException, InterruptedException;
        /**
         * Получение по идентификатору.
         */
        Subtask getSubtask(int id) throws IOException, InterruptedException;
        /**
         * Создание подзадачи.Сам объект должен передаваться в качестве параметра.
         * @param subtask
         */
        int creatingSubtask(Subtask subtask) throws IOException, InterruptedException;
        /**
         * Обновление подзадачи. Новая версия объекта с верным идентификатором передаются в виде параметра.
         * @param subtask
         */
        void updateSubtask(Subtask subtask) throws IOException, InterruptedException;
        /**
         * Удаление подзадачи по идентификатору.
         */
        void deleteSubtaskId(int id) throws IOException, InterruptedException;

        /**
         * Получение списка всех Epics.
         */
        List<Epic> getEpics() throws IOException, InterruptedException;
        /**
         * Удаление всех Epics.
         */
        void deleteEpics() throws IOException, InterruptedException;
        /**
         * Получение по идентификатору.
         */
        Epic getEpic(int id) throws IOException, InterruptedException;
        /**
         * Создание Epic.Сам объект должен передаваться в качестве параметра.
         * @param epic
         */
        int creatingEpic(Epic epic) throws IOException, InterruptedException;
        /**
         * Обновление tasksOfDifferentTypes.Epic. Новая версия объекта с верным идентификатором передаются в виде параметра.
         * @param epic
         */
        void updateEpic(Epic epic) throws IOException, InterruptedException;
        /**
         * Удаление по идентификатору.
         */
        void deleteEpicId(int id) throws IOException, InterruptedException;
        /**
         * Получение списка всех подзадач определённого эпика.
         * @param epic
         */
        ArrayList<Subtask> getSubtasksEpic(Epic epic) throws IOException, InterruptedException;

        /**
         * Получение истории просмотра задач.
         *
         */
        List<Task> getHistory() throws IOException, InterruptedException;

        /**
         * Сортировка задач.
         */
        List<Task> getPrioritizedTasks();
    }
