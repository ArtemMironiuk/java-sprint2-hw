package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import json.DurationAdapter;
import json.LocalDateTimeAdapter;
import manager.FileBackedTasksManager;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTasksManager {
    private String url;
    private KVClient kvClient;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskManager(String url) {
        super(url);
        this.url = url;
        kvClient = new KVClient(url);
    }
//    запись
    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(mapTasks.values()));
        kvClient.put("tasks", jsonTasks);
        String jsonSubtasks = gson.toJson(new ArrayList<>(mapSubtasks.values()));
        kvClient.put("subtasks", jsonSubtasks);
        String jsonEpics = gson.toJson(new ArrayList<>(mapEpics.values()));
        kvClient.put("epics", jsonEpics);
        String jsonHistory = gson.toJson(new ArrayList<>(inMemoryHistoryManager.getHistory()));
        kvClient.put("history", jsonHistory);

    }

    //чтение
    @Override
    protected void load() {
        ArrayList<Task> tasks = gson.fromJson(kvClient.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        if(!tasks.isEmpty()) {
            for (Task task : tasks) {
                mapTasks.put(task.getId(), task);
                prioritizedTasks.add(task);
            }
        }
        ArrayList<Epic> epics = gson.fromJson(kvClient.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        if(!epics.isEmpty()) {
            for (Epic epic : epics) {
                mapEpics.put(epic.getId(), epic);
            }
        }
        ArrayList<Subtask> subtasks = gson.fromJson(kvClient.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        if(!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                mapSubtasks.put(subtask.getId(), subtask);
                prioritizedTasks.add(subtask);
                if(mapEpics.containsKey(subtask.getEpicId())) {
                    mapEpics.get(subtask.getEpicId()).getSubtask().add(subtask);
                }
            }
        }

        ArrayList<Task> history = gson.fromJson(kvClient.load("history"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        if(!history.isEmpty()) {
            for (Task task : history) {
                inMemoryHistoryManager.add(task);
            }
        }

    }

    public static HttpTaskManager loadFromUrl (String uri) {
        HttpTaskManager manager = new HttpTaskManager(uri);
        manager.load();
        return manager;
    }
}
