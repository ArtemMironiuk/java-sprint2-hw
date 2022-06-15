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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private String url;
    private KVClient kvClient;
    private String key = "manager";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

//    public HttpTaskManager(String url) throws IOException, InterruptedException {
//        this.url = url;
//        kvClient = new KVClient();
//    }

    public HttpTaskManager(String url) {
        super(url);
        this.url = url;
        kvClient = new KVClient(url);
    }
//    запись
    @Override
    protected void save() {
//        final String jsonMapTasks = gson.toJson(mapTasks);
//        final String jsonMapSubtasks = gson.toJson(mapSubtasks);
//        final String jsonMapEpics = gson.toJson(mapEpics);
//        final String jsonListHistory = gson.toJson(inMemoryHistoryManager.getHistory());
//        kvClient.put("Tasks", jsonMapTasks);
//        kvClient.put("Subtasks",jsonMapSubtasks);
//        kvClient.put("Epics",jsonMapEpics);
//        kvClient.put("History",jsonListHistory);
//        super.save();
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gson.toJson(mapTasks));
        stringBuilder.append(";");
        stringBuilder.append(gson.toJson(mapSubtasks));
        stringBuilder.append(";");
        stringBuilder.append(gson.toJson(mapEpics));
        stringBuilder.append(";");
        stringBuilder.append(gson.toJson(inMemoryHistoryManager.getHistory()));

        kvClient.put(key,stringBuilder.toString());
    }

    //чтение
    @Override
    protected void load() {
//        String jsonTasks = kvClient.load("Tasks");
//        String jsonSubtasks = kvClient.load("Subtasks");
//        String jsonEpics = kvClient.load("Epics");
//        String jsonHistory = kvClient.load("History");
//        super.load();
//        mapTasks = gson.fromJson(jsonTasks, new TypeToken<HashMap <Integer, Task>>() {
//        }.getType());
//        mapSubtasks = gson.fromJson(jsonTasks, new TypeToken<HashMap <Integer, Subtask>>() {
//        }.getType());
//        mapEpics = gson.fromJson(jsonTasks, new TypeToken<HashMap <Integer, Epic>>() {
//        }.getType());
//         = gson.fromJson(jsonTasks, new TypeToken<HashMap <Integer, Task>>() {
//        }.getType());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
