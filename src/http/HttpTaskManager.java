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
import java.util.HashMap;

public class HttpTaskManager extends FileBackedTasksManager {
    private String url;
    private KVClient kvClient;
    private String key = "manager";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskManager(String url) {
        super(url);
        this.url = url;
        kvClient = new KVClient(url);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
//    запись
    @Override
    protected void save() {
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
        String jsonManager = kvClient.load(key);
        String[] split = jsonManager.split(";");
        mapTasks = gson.fromJson(split[0],new TypeToken<HashMap <Integer, Task>>() {
        }.getType());
        mapSubtasks = gson.fromJson(split[1], new TypeToken<HashMap <Integer, Subtask>>() {
        }.getType());
        mapEpics = gson.fromJson(split[2], new TypeToken<HashMap <Integer, Epic>>() {
        }.getType());
        inMemoryHistoryManager = gson.fromJson(split[3], new TypeToken<HashMap <Integer, Task>>() {
        }.getType());

    }

    public static HttpTaskManager loadFromUrl (String uri) {
        HttpTaskManager manager = new HttpTaskManager(uri);
        manager.load();
        return manager;
    }
}
