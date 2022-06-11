package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import json.DurationAdapter;
import json.LocalDateTimeAdapter;
import manager.Managers;
import manager.TaskManager;
import tasksOfDifferentTypes.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;
import static utils.Status.NEW;

public class HttpTaskServer { //18 эндпоинтов и в тестах сделать клиента на этот класс
public static final int PORT = 8080;
    HttpServer httpServer;
    TaskManager manager = Managers.getDefaultTaskManager();

    public HttpTaskServer() throws IOException,InterruptedException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.start();
        httpServer.createContext("/tasks/",this::handlerTasks);
        httpServer.createContext("/tasks/task", this :: handler);
        httpServer.createContext("/tasks/");
        httpServer.createContext("/tasks/");
        httpServer.createContext("/tasks/");
        System.out.println("Сервер запущен на 8080 порту");
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

    private void handlerTasks(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());
            Gson gson = getGson();
            String path = h.getRequestURI().getPath();
            String serializeTasks = null;
            String serializeSubtasks = null;
            String serializeEpic = null;
            if ("GET".equals(h.getRequestMethod())) {
                serializeTasks = gson.toJson(manager.getTasks());
                serializeSubtasks = gson.toJson(manager.getSubtasks());
                serializeEpic = gson.toJson(manager.getEpics());
                h.sendResponseHeaders(200, 0);
                OutputStream os = h.getResponseBody();
                if (serializeTasks != null) {
                    os.write(serializeTasks.getBytes(StandardCharsets.UTF_8));
                }
                if (serializeSubtasks != null) {
                    os.write(serializeSubtasks.getBytes(StandardCharsets.UTF_8));
                }
                if (serializeEpic != null) {
                    os.write(serializeEpic.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                System.out.println("/tasks/ ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    private void handler(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());
            Gson gson = getGson();
            String path = h.getRequestURI().getPath();
            String serializeTasks = null;
            if ("GET".equals(h.getRequestMethod())) {
                serializeTasks = gson.toJson(manager.getTasks());
                h.sendResponseHeaders(200, 0);
                OutputStream os = h.getResponseBody();
                if (serializeTasks != null) {
                    os.write(serializeTasks.getBytes(StandardCharsets.UTF_8));
                }
            } else if ("DELETE".equals(h.getRequestMethod())) {
                manager.deleteTasks();
                if (manager.getTasks().isEmpty()) {
                    h.sendResponseHeaders(200, 0);
                }
            } else if ("POST".equals(h.getRequestMethod())) {
                InputStream inputStream = h.getRequestBody();
                String body = new String(inputStream.readAllBytes(),DEFAULT_CHARSET);
                Task task = gson.fromJson(body,Task.class);
                manager.creatingTask(task);
                h.sendResponseHeaders(201,0);

            }
            // Реализация
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
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
