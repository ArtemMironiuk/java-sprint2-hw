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
    URI url;
    KVClient kvClient;

    public HttpTaskManager(URI url) throws IOException, InterruptedException {
        this.url = url;
        kvClient = new KVClient();
    }

    public HttpTaskManager() {
        super();
    }
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

    @Override
    public List<Task> getTasks() throws IOException, InterruptedException {
        return super.getTasks();
    }

    @Override
    public void deleteTasks() throws IOException, InterruptedException {
        super.deleteTasks();
    }

    @Override
    public Task getTask(int id) throws IOException, InterruptedException {
        return super.getTask(id);
    }

    @Override
    public int creatingTask(Task task) throws IOException, InterruptedException {
        return super.creatingTask(task);
    }

    @Override
    public void updateTask(Task task) throws IOException, InterruptedException {
        super.updateTask(task);
    }

    @Override
    public void deleteTaskId(int id) throws IOException, InterruptedException {
        super.deleteTaskId(id);
    }

    @Override
    public List<Subtask> getSubtasks() throws IOException, InterruptedException {
        return super.getSubtasks();
    }

    @Override
    public void deleteSubtasks() throws IOException, InterruptedException {
        super.deleteSubtasks();
    }

    @Override
    public Subtask getSubtask(int id) throws IOException, InterruptedException {
        return super.getSubtask(id);
    }

    @Override
    public int creatingSubtask(Subtask subtask) throws IOException, InterruptedException {
        return super.creatingSubtask(subtask);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException, InterruptedException {
        super.updateSubtask(subtask);
    }

    @Override
    public void deleteSubtaskId(int id) throws IOException, InterruptedException {
        super.deleteSubtaskId(id);
    }

    @Override
    public List<Epic> getEpics() throws IOException, InterruptedException {
        return super.getEpics();
    }

    @Override
    public void deleteEpics() throws IOException, InterruptedException {
        super.deleteEpics();
    }

    @Override
    public Epic getEpic(int id) throws IOException, InterruptedException {
        return super.getEpic(id);
    }

    @Override
    public int creatingEpic(Epic epic) throws IOException, InterruptedException {
        return super.creatingEpic(epic);
    }

    @Override
    public void updateEpic(Epic epic) throws IOException, InterruptedException {
        super.updateEpic(epic);
    }

    @Override
    public void deleteEpicId(int id) throws IOException, InterruptedException {
        super.deleteEpicId(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasksEpic(Epic epic) throws IOException, InterruptedException {
        return super.getSubtasksEpic(epic);
    }

    @Override
    public List<Task> getHistory() throws IOException, InterruptedException {
        return super.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

//    запись
    @Override
    protected void save() throws IOException, InterruptedException {
        Gson gson = getGson();
//        final String jsonMapTasks = gson.toJson(mapTasks);
//        final String jsonMapSubtasks = gson.toJson(mapSubtasks);
//        final String jsonMapEpics = gson.toJson(mapEpics);
//        final String jsonListHistory = gson.toJson(inMemoryHistoryManager.getHistory());
//        kvClient.save("Tasks", jsonMapTasks);
//        kvClient.save("Subtasks",jsonMapSubtasks);
//        kvClient.save("Epics",jsonMapEpics);
//        kvClient.save("History",jsonListHistory);
//        super.save();
    }

    //чтение
    @Override
    protected void load() throws IOException, InterruptedException {
        Gson gson = getGson();
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
}
