package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import json.DurationAdapter;
import json.LocalDateTimeAdapter;
import manager.Managers;
import manager.TaskManager;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
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
private String id;
private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .registerTypeAdapter(Duration.class, new DurationAdapter())
        .create();

    HttpServer httpServer;
    TaskManager manager = Managers.getDefaultTaskManager();

    public HttpTaskServer() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
 //           httpServer.createContext("/tasks/",this::handlerTasks);
            httpServer.createContext("/tasks/task", this :: handlerTask);
//            httpServer.createContext("/tasks/task/?id=", this::handlerIdTask);
            httpServer.createContext("/tasks/subtask", this::handlerSubtask);
//            httpServer.createContext("/tasks/subtask/?id=", this::handlerIdSubtask);
            httpServer.createContext("/tasks/epic",this::handlerEpic);
//            httpServer.createContext("/tasks/epic/?id=",this::handlerIdEpic);
            httpServer.createContext("/tasks/history",this::handlerHistory);
            httpServer.createContext("/tasks",this::handlerPrioritized);
            httpServer.createContext("/tasks/subtask/epic", this::handlerSubtaskFromEpic);

        } catch (IOException e) {
            System.out.println("Сервер не запущен на 8080 порту");
        }

    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpServer.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен на порту " + PORT);
        httpServer.stop(0);
    }

    protected void handlerTasks(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());
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

    protected void handlerTask(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());

            String path = h.getRequestURI().getPath();
            String serializeTasks = null;
            String method = h.getRequestMethod();
//            switch (method) {
//                case "GET":
//                    break;
//                case "POST":
//                    break;
//                case "DELETE":
//                    break;
//            }
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
                if (body.isEmpty()) {
                    System.out.println("Тело для добовление или обновления task пустое. Тело указывается в body запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                Task task = gson.fromJson(body,Task.class);
                if (manager.getTask(task.getId()) != null) {
                    manager.updateTask(task);
                } else {
                    manager.creatingTask(task);
                }
                h.sendResponseHeaders(201,0);
                //послать боди или команду

            } else {
                System.out.println("/tasks/task ждёт GET, POST, DELETE - запросы, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
            // Реализация
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    protected void handlerIdTask(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());
            //сделать в одном методе с тасками
            String path = h.getRequestURI().getRawQuery();
            String[] split = path.split("=");
            id = split[1];
            int idParsed = Integer.parseInt(id);
            String serializeTask = null;
            if ("GET".equals(h.getRequestMethod())) {
                if (path != null && (path.contains("id=" + id))) {
                    serializeTask = gson.toJson(manager.getTask(idParsed));
                    h.sendResponseHeaders(200, 0);
                    OutputStream os = h.getResponseBody();
                    if (serializeTask != null) {
                        os.write(serializeTask.getBytes(StandardCharsets.UTF_8));
                    }
                } else {
                    System.out.println("Эндпоинт не соответствует. Проверьте указанный id.");
                    h.sendResponseHeaders(400, 0);
                }
                id = null;
            } else if ("DELETE".equals(h.getRequestMethod())) {
                if (path != null && (path.contains("id=" + id))) {
                    manager.deleteTaskId(idParsed);
                    if (manager.getTask(idParsed) == null) {
                        h.sendResponseHeaders(200, 0);
                    } else {
                        System.out.println("task по id не удален.");
                        h.sendResponseHeaders(401,0);
                    }
                    id = null;
                } else {
                    System.out.println("Эндпоинт не соответствует. Проверьте указанный id.");
                    h.sendResponseHeaders(400, 0);
                }
            } else  {
                System.out.println("/tasks/task/?id= ждёт GET - запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
            // Реализация
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    protected void handlerSubtask(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());

            String path = h.getRequestURI().getPath();
            String serializeSubtasks = null;
            if ("GET".equals(h.getRequestMethod())) {
                serializeSubtasks = gson.toJson(manager.getSubtasks());
                h.sendResponseHeaders(200, 0);
                OutputStream os = h.getResponseBody();
                if (serializeSubtasks != null) {
                    os.write(serializeSubtasks.getBytes(StandardCharsets.UTF_8));
                }
            } else if ("DELETE".equals(h.getRequestMethod())) {
                manager.deleteSubtasks();
                if (manager.getSubtasks().isEmpty()) {
                    h.sendResponseHeaders(200, 0);
                }
            } else if ("POST".equals(h.getRequestMethod())) {
                InputStream inputStream = h.getRequestBody();
                String body = new String(inputStream.readAllBytes(),DEFAULT_CHARSET);
                if (body.isEmpty()) {
                    System.out.println("Тело для добовление или обновления subtask пустое. Тело указывается в body запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                Subtask subtask = gson.fromJson(body, Subtask.class);
                if (manager.getSubtask(subtask.getId()) != null) {
                    manager.updateSubtask(subtask);
                } else {
                    manager.creatingSubtask(subtask);
                }
                h.sendResponseHeaders(201,0);
            } else {
                System.out.println("/tasks/subtask ждёт GET, POST, DELETE - запросы, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
            // Реализация
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    protected void handlerIdSubtask(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());

            String path = h.getRequestURI().getRawQuery();
            String[] split = path.split("=");
            id = split[2];
            int idParsed = Integer.parseInt(id);
            String serializeSubtask = null;
            if ("GET".equals(h.getRequestMethod())) {
                if (path != null && (path.contains("id=" + id))) {
                    serializeSubtask = gson.toJson(manager.getSubtask(idParsed));
                    h.sendResponseHeaders(200, 0);
                    OutputStream os = h.getResponseBody();
                    if (serializeSubtask != null) {
                        os.write(serializeSubtask.getBytes(StandardCharsets.UTF_8));
                    }
                } else {
                    System.out.println("Эндпоинт не соответствует. Проверьте указанный id.");
                    h.sendResponseHeaders(400, 0);
                }
                id = null;
            } else if ("DELETE".equals(h.getRequestMethod())) {
                if (path != null && (path.contains("id=" + id))) {
                    manager.deleteSubtaskId(idParsed);
                    if (manager.getSubtask(idParsed) == null) {
                        h.sendResponseHeaders(200, 0);
                    } else {
                        System.out.println("task по id не удален.");
                        h.sendResponseHeaders(401,0);
                    }
                    id = null;
                } else {
                    System.out.println("Эндпоинт не соответствует. Проверьте указанный id.");
                    h.sendResponseHeaders(400, 0);
                }
            } else  {
                System.out.println("/tasks/subtask/?id= ждёт GET - запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
            // Реализация
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    protected void handlerEpic(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());
            String path = h.getRequestURI().getPath();
            String serializeEpic = null;
            if ("GET".equals(h.getRequestMethod())) {
                serializeEpic = gson.toJson(manager.getEpics());
                h.sendResponseHeaders(200, 0);
                OutputStream os = h.getResponseBody();
                if (serializeEpic != null) {
                    os.write(serializeEpic.getBytes(StandardCharsets.UTF_8));
                }
            } else if ("DELETE".equals(h.getRequestMethod())) {
                manager.deleteEpics();
                if (manager.getEpics().isEmpty()) {
                    h.sendResponseHeaders(200, 0);
                }
            } else if ("POST".equals(h.getRequestMethod())) {
                InputStream inputStream = h.getRequestBody();
                String body = new String(inputStream.readAllBytes(),DEFAULT_CHARSET);
                if (body.isEmpty()) {
                    System.out.println("Тело для добовление или обновления epic пустое. Тело указывается в body запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                Epic epic = gson.fromJson(body, Epic.class);
                if (manager.getEpic(epic.getId()) != null){
                    manager.updateEpic(epic);
                } else {
                    manager.creatingEpic(epic);
                }
                h.sendResponseHeaders(201,0);
            } else {
                System.out.println("/tasks/epic ждёт GET, POST, DELETE - запросы, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
            // Реализация
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    protected void handlerIdEpic(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());

            String path = h.getRequestURI().getRawQuery();
            String[] split = path.split("=");
            id = split[2];
            int idParsed = Integer.parseInt(id);
            String serializeEpic = null;
            if ("GET".equals(h.getRequestMethod())) {
                if (path != null && (path.contains("id=" + id))) {
                    serializeEpic = gson.toJson(manager.getEpic(idParsed));
                    h.sendResponseHeaders(200, 0);
                    OutputStream os = h.getResponseBody();
                    if (serializeEpic != null) {
                        os.write(serializeEpic.getBytes(StandardCharsets.UTF_8));
                    }
                } else {
                    System.out.println("Эндпоинт не соответствует. Проверьте указанный id.");
                    h.sendResponseHeaders(400, 0);
                }
                id = null;
            } else if ("DELETE".equals(h.getRequestMethod())) {
                if (path != null && (path.contains("id=" + id))) {
                    manager.deleteEpicId(idParsed);
                    if (manager.getEpic(idParsed) == null) {
                        h.sendResponseHeaders(200, 0);
                    } else {
                        System.out.println("task по id не удален.");
                        h.sendResponseHeaders(401,0);
                    }
                    id = null;
                } else {
                    System.out.println("Эндпоинт не соответствует. Проверьте указанный id.");
                    h.sendResponseHeaders(400, 0);
                }
            } else  {
                System.out.println("/tasks/subtask/?id= ждёт GET - запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
            // Реализация
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    protected void handlerHistory(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());

            String path = h.getRequestURI().getPath();
            String serializeHistory = null;
            if ("GET".equals(h.getRequestMethod())) {
                serializeHistory = gson.toJson(manager.getHistory());
                h.sendResponseHeaders(200, 0);
                OutputStream os = h.getResponseBody();
                if (serializeHistory != null) {
                    os.write(serializeHistory.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                System.out.println("/tasks/history ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
            // Реализация
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    protected void handlerPrioritized(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());

            String path = h.getRequestURI().getPath();
            String serializePrioritizedTasks = null;
            if ("GET".equals(h.getRequestMethod())) {
                serializePrioritizedTasks = gson.toJson(manager.getPrioritizedTasks());
                h.sendResponseHeaders(200, 0);
                OutputStream os = h.getResponseBody();
                if (serializePrioritizedTasks != null) {
                    os.write(serializePrioritizedTasks.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                System.out.println("/tasks/prioritized ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
            // Реализация
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }




//    TaskManager manager = Managers.getDefaultTaskManager();

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();

//        сохраняем отдельно задачи, сабтаски и


    }
}
