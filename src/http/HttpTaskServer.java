package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import json.DurationAdapter;
import json.LocalDateTimeAdapter;
import manager.Managers;
import manager.TaskManager;
import tasksOfDifferentTypes.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import static utils.Status.NEW;

public class HttpTaskServer {
//    public HttpTaskServer() throws IOException, InterruptedException {
//    } //18 эндпоинтов и в тестах сделать клиента на этот класс
public static final int PORT = 8080;
    HttpServer httpServer;
    TaskManager manager = Managers.getDefaultTaskManager();

    public HttpTaskServer() throws IOException,InterruptedException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.start();
        httpServer.createContext("/tasks/");
        System.out.println("Сервер запущен на 8080 порту");
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }



//    TaskManager manager = Managers.getDefaultTaskManager();

    public static void main(String[] args) throws IOException,InterruptedException {
        new HttpTaskServer();
        final  Gson gson = getGson();
        Task task = new Task("Тест", "Описание",NEW,1, LocalDateTime.now(), Duration.ofMinutes(15));
        final HashMap<Integer, Task> map = new HashMap<>();
        map.put(task.getId(), task);
        final String json = gson.toJson(map);
        System.out.println(json);
        final HashMap<Integer, Task> mapRest = gson.fromJson(json, new TypeToken<HashMap <Integer, Task>>() {
                }.getType());
        System.out.println(mapRest);

//        сохраняем отдельно задачи, сабтаски и


    }
}
