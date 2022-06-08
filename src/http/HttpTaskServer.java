package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import tasksOfDifferentTypes.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import static utils.Status.NEW;

public class HttpTaskServer {

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

//    TaskManager manager = Managers.getDefaultTaskManager();
//
//    public HttpTaskServer() throws IOException {
//        HttpServer httpServer = HttpServer.create();
//        httpServer.bind(new InetSocketAddress(8080), 0);
////        httpServer.createContext("/hello",new ....)
//        httpServer.start();
//        System.out.println("Сервер запущен на 8080 порту");
//    }
    public static void main(String[] args) {
        final  Gson gson = getGson();
        Task task = new Task("Тест", "Описание",NEW,1, LocalDateTime.now(), Duration.ofMinutes(15));
        final HashMap<Integer, Task> map = new HashMap<>();
        map.put(task.getId(), task);
        final String json = gson.toJson(map);
        System.out.println(json);
        final HashMap<Integer, Task> mapRest = gson.fromJson(json, new TypeToken<HashMap <Integer, Task>>() {
                }.getType());
        System.out.println(mapRest);

        //сохраняем отдельно задачи, сабтаски и


    }
}
