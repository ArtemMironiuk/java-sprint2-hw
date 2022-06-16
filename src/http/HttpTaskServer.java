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

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

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
            httpServer.createContext("/tasks/task", this::handlerTask);
            httpServer.createContext("/tasks/subtask", this::handlerSubtask);
            httpServer.createContext("/tasks/epic", this::handlerEpic);
            httpServer.createContext("/tasks/history", this::handlerHistory);
            httpServer.createContext("/tasks", this::handlerPrioritized);
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

    protected void handlerTask(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());

            String path = h.getRequestURI().getPath();
            String pathId = h.getRequestURI().getRawQuery();
            String serializeTasks = null;
            String method = h.getRequestMethod();

            if (pathId != null) {
                String[] split = pathId.split("=");
                id = split[1];
                int idParsed = Integer.parseInt(id);
                String serializeTask = null;
                switch (method) {
                    case "GET":
                        if (manager.getTask(idParsed) != null) {
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
                        break;
                    case "DELETE":
                        if (manager.getTask(idParsed) != null) {
                            manager.deleteTaskId(idParsed);
                            if (manager.getTask(idParsed) == null) {
                                System.out.println("task c id = " + id + " удален!");
                                h.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("task по id не удален.");
                                h.sendResponseHeaders(401, 0);
                            }
                            id = null;
                        } else {
                            System.out.println("Эндпоинт не соответствует. Проверьте указанный id.");
                            h.sendResponseHeaders(400, 0);
                        }
                        break;
                    default:
                        System.out.println("/tasks/task/?id= ждёт GET или DELETE - запрос, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } else {
                switch (method) {
                    case "GET":
                        serializeTasks = gson.toJson(manager.getTasks());
                        h.sendResponseHeaders(200, 0);
                        OutputStream os = h.getResponseBody();
                        if (serializeTasks != null) {
                            os.write(serializeTasks.getBytes(StandardCharsets.UTF_8));
                        }
                        break;
                    case "POST":
                        InputStream inputStream = h.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        if (body.isEmpty()) {
                            System.out.println("Тело для добовление или обновления task пустое. Тело указывается в body запроса");
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        Task task = gson.fromJson(body, Task.class);
                        if (manager.getTask(task.getId()) == null) {
                            manager.creatingTask(task);
                        } else {
                            manager.updateTask(task);
                        }
                        System.out.println("Тело передано на сервер");
                        h.sendResponseHeaders(201, 0);
                        break;
                    case "DELETE":
                        manager.deleteTasks();
                        if (manager.getTasks().isEmpty()) {
                            h.sendResponseHeaders(200, 0);
                        }
                        break;
                    default:
                        System.out.println("/tasks/task ждёт GET, POST, DELETE - запросы, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            }
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
            String pathId = h.getRequestURI().getRawQuery();
            String serializeSubtasks = null;
            String method = h.getRequestMethod();

            if (pathId != null) {
                String[] split = pathId.split("=");
                id = split[1];
                int idParsed = Integer.parseInt(id);
                String serializeSubtask = null;
                switch (method) {
                    case "GET":
                        if (manager.getSubtask(idParsed) != null) {
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
                        break;
                    case "DELETE":
                        if (manager.getSubtask(idParsed) != null) {
                            manager.deleteSubtaskId(idParsed);
                            if (manager.getSubtask(idParsed) == null) {
                                System.out.println("subtask c id = " + id + " удален!");
                                h.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("subtask по id не удален.");
                                h.sendResponseHeaders(401, 0);
                            }
                            id = null;
                        } else {
                            System.out.println("Эндпоинт не соответствует. Проверьте указанный id.");
                            h.sendResponseHeaders(400, 0);
                        }
                        break;
                    default:
                        System.out.println("/tasks/subtask/?id= ждёт GET или DELETE - запрос, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } else {
                switch (method) {
                    case "GET":
                        if (manager.getSubtasks() != null) {
                            serializeSubtasks = gson.toJson(manager.getSubtasks());
                            h.sendResponseHeaders(200, 0);
                            OutputStream os = h.getResponseBody();
                            if (serializeSubtasks != null) {
                                os.write(serializeSubtasks.getBytes(StandardCharsets.UTF_8));
                            }
                        }
                        break;
                    case "POST":
                        InputStream inputStream = h.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        if (body.isEmpty()) {
                            System.out.println("Тело для добовление или обновления subtask пустое. Тело указывается в body запроса");
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        if (manager.getSubtask(subtask.getId()) == null) {
                            manager.creatingSubtask(subtask);
                        } else {
                            manager.updateSubtask(subtask);
                        }
                        System.out.println("Тело передано на сервер");
                        h.sendResponseHeaders(201, 0);
                        break;
                    case "DELETE":
                        manager.deleteSubtasks();
                        if (manager.getSubtasks().isEmpty()) {
                            h.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Не удалось удалить subtasks");
                            h.sendResponseHeaders(400, 0);
                        }
                        break;
                    default:
                        System.out.println("/tasks/subtask ждёт GET, POST, DELETE - запросы, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            }
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
            String pathId = h.getRequestURI().getRawQuery();
            String serializeEpics = null;
            String method = h.getRequestMethod();

            if (pathId != null) {
                String[] split = pathId.split("=");
                id = split[1];
                int idParsed = Integer.parseInt(id);
                String serializeEpic = null;
                switch (method) {
                    case "GET":
                        if (manager.getEpic(idParsed) != null) {
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
                        break;
                    case "DELETE":
                        if (manager.getEpic(idParsed) != null) {
                            manager.deleteEpicId(idParsed);
                            if (manager.getEpic(idParsed) == null) {
                                System.out.println("epic c id = " + id + " удален!");
                                h.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("epic по id не удален.");
                                h.sendResponseHeaders(401, 0);
                            }
                            id = null;
                        } else {
                            System.out.println("Эндпоинт не соответствует. Проверьте указанный id.");
                            h.sendResponseHeaders(400, 0);
                        }
                        break;
                    default:
                        System.out.println("/tasks/epic/?id= ждёт GET или DELETE - запрос, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } else {
                switch (method) {
                    case "GET":
                        if (manager.getEpics() != null) {
                            serializeEpics = gson.toJson(manager.getEpics());
                            h.sendResponseHeaders(200, 0);
                            OutputStream os = h.getResponseBody();
                            if (serializeEpics != null) {
                                os.write(serializeEpics.getBytes(StandardCharsets.UTF_8));
                            }
                        }
                        break;
                    case "POST":
                        InputStream inputStream = h.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        if (body.isEmpty()) {
                            System.out.println("Тело для добовление или обновления subtask пустое. Тело указывается в body запроса");
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (manager.getEpic(epic.getId()) == null) {
                            manager.creatingEpic(epic);
                        } else {
                            manager.updateEpic(epic);
                        }
                        System.out.println("Тело передано на сервер");
                        h.sendResponseHeaders(201, 0);
                        break;
                    case "DELETE":
                        manager.deleteEpics();
                        if (manager.getEpics().isEmpty()) {
                            h.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Не удалось удалить epic");
                            h.sendResponseHeaders(400, 0);
                        }
                        break;
                    default:
                        System.out.println("/tasks/epic ждёт GET, POST, DELETE - запросы, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

        protected void handlerHistory (HttpExchange h){
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

        protected void handlerPrioritized (HttpExchange h){
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

    protected void handlerSubtaskFromEpic(HttpExchange h) {
        try {
            System.out.println(h.getRequestURI());

            String path = h.getRequestURI().getPath();
            String pathId = h.getRequestURI().getRawQuery();
            String serializeListSubtasks = null;

            if (pathId != null) {
                String[] split = pathId.split("=");
                id = split[1];
                int idParsed = Integer.parseInt(id);
                String serializeEpic = null;

                if ("GET".equals(h.getRequestMethod())) {
                    final Epic epic = manager.getEpic(idParsed);
                    serializeListSubtasks = gson.toJson(manager.getSubtasksEpic(epic));
                    h.sendResponseHeaders(200, 0);
                    OutputStream os = h.getResponseBody();
                    if (serializeListSubtasks != null) {
                        os.write(serializeListSubtasks.getBytes(StandardCharsets.UTF_8));
                    }
                } else {
                    System.out.println("/tasks/subtask/epic/?id= ждёт GET-запрос, а получил " + h.getRequestMethod());
                    h.sendResponseHeaders(405, 0);
                    id = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

//        public static void main (String[]args) throws IOException {
//            new KVServer().start();
//            new HttpTaskServer().start();
//
////        сохраняем отдельно задачи, сабтаски и
//
//
//        }
}
