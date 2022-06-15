    package manager;

    import http.HttpTaskManager;

    import java.io.File;
    import java.io.IOException;
    import java.net.URI;

    /**
     * Менеджеры.
     */
    public class Managers {

        public static TaskManager getDefaultTaskManager()  {
            return new HttpTaskManager("http://localhost:8078/");
        }

        public static HistoryManager getDefaultHistory(){
            return new InMemoryHistoryManager();
        }
    }
