    package manager;

    import java.io.File;

    /**
     * Менеджеры.
     */
    public class Managers {

        public static TaskManager getDefaultTaskManager() {
            return new FileBackedTasksManager(new File("test.csv"));
        }

        public static HistoryManager getDefaultHistory(){
            return new InMemoryHistoryManager();
        }
    }
