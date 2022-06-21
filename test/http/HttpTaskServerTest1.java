package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.DurationAdapter;
import json.LocalDateTimeAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static utils.Status.NEW;

public class HttpTaskServerTest1 {
    private static HttpTaskServer httpTaskServer;
    private static KVServer kvServer;
    private static Gson gson;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer =new HttpTaskServer();
        httpTaskServer.start();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @AfterEach
    public void afterEach(){
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    public void newTaskTest () {
        //Тест на запрос POST
        HttpClient client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task");
        Task newTask = new Task("Тест", "Описание", NEW, 0, LocalDateTime.of(2022, 6, 11, 15, 50, 10), Duration.ofMinutes(30));
        String jsonTask = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet = response.statusCode();

        Assertions.assertEquals(201, statusCodeGet, "Получен статус код " + statusCodeGet);

        //Тест на запрос GET
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        HttpResponse<String> response1 = null;
        try {
            response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet1 = response1.statusCode();

        Assertions.assertEquals(200, statusCodeGet1, "Получен статус код " + statusCodeGet1);
        Assertions.assertEquals(gson.toJson(httpTaskServer.manager.getTasks()), response1.body(), "Получено body " + response1.body() );

        //Тест на запрос DELETE
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url1)
                .DELETE()
                .build();
        HttpResponse<String> response2 = null;
        try {
            response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet2 = response2.statusCode();

        Assertions.assertEquals(200, statusCodeGet2, "Получен статус код " + statusCodeGet2);
        Assertions.assertEquals(0, httpTaskServer.manager.getTasks().size());

        //Тест на запрос PUT, а такого запроса нет
        Task newTask1 = new Task("Тест", "Описание", NEW, 0, LocalDateTime.of(2022, 6, 11, 15, 50, 10), Duration.ofMinutes(30));
        String jsonTask1 = gson.toJson(newTask1);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(jsonTask1);
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url1)
                .PUT(body1)
                .build();
        HttpResponse<String> response3 = null;
        try {
            response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet3 = response3.statusCode();

        Assertions.assertEquals(405, statusCodeGet3, "Получен статус код " + statusCodeGet);
    }

    @Test
    public void newTaskTestId () {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task newTask = new Task("Тест", "Описание", NEW, 0, LocalDateTime.of(2022, 6, 11, 15, 50, 10), Duration.ofMinutes(30));
        String jsonTask = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet = response.statusCode();

        Assertions.assertEquals(201, statusCodeGet, "Получен статус код " + statusCodeGet);

        //Тест на GET запрос по Id проверка статус кода 200
        URI url1 = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        HttpResponse<String> response1 = null;
        try {
            response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet1 = response1.statusCode();

        Assertions.assertEquals(200, statusCodeGet1, "Получен статус код " + statusCodeGet1);
        Assertions.assertEquals(gson.toJson(httpTaskServer.manager.getTask(1)), response1.body(), "Получено body " + response1.body() );

        //Тест на GET запрос по Id проверка статус кода 400
        URI url2 = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpResponse<String> response2 = null;
        try {
            response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet2 = response2.statusCode();

        Assertions.assertEquals(400, statusCodeGet2, "Получен статус код " + statusCodeGet2);

        //Тест на запрос DELETE по id
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url1)
                .DELETE()
                .build();
        HttpResponse<String> response3 = null;
        try {
            response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet3 = response3.statusCode();

        Assertions.assertEquals(200, statusCodeGet3, "Получен статус код " + statusCodeGet3);
        Assertions.assertEquals(null, httpTaskServer.manager.getTask(1));

        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(url1)
                .DELETE()
                .build();
        HttpResponse<String> response4 = null;
        try {
            response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet4 = response4.statusCode();

        Assertions.assertEquals(400, statusCodeGet4, "Получен статус код " + statusCodeGet4);
        Assertions.assertNull(httpTaskServer.manager.getTask(1));
    }

    @Test
    public void newSubtaskTest () {
        //Тест на запрос POST
        HttpClient client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/subtask");
        Subtask newSubtask = new Subtask("Тест", "Описание", NEW,0, 0,LocalDateTime.of(2022,5,27,18,00,10), Duration.ofMinutes(15));
        String jsonSubtask = gson.toJson(newSubtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet = response.statusCode();

        Assertions.assertEquals(201, statusCodeGet, "Получен статус код " + statusCodeGet);

        //Тест на запрос GET
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        HttpResponse<String> response1 = null;
        try {
            response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet1 = response1.statusCode();

        Assertions.assertEquals(200, statusCodeGet1, "Получен статус код " + statusCodeGet1);
        Assertions.assertEquals(gson.toJson(httpTaskServer.manager.getSubtasks()), response1.body(), "Получено body " + response1.body() );

        //Тест на запрос PUT, а такого запроса нет
        Subtask newSubtask1 = new Subtask("Тест", "Описание", NEW,0, 0,LocalDateTime.of(2022,5,27,18,00,10), Duration.ofMinutes(15));
        String jsonSubtask1 = gson.toJson(newSubtask1);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(jsonSubtask1);
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url1)
                .PUT(body1)
                .build();
        HttpResponse<String> response3 = null;
        try {
            response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet3 = response3.statusCode();

        Assertions.assertEquals(405, statusCodeGet3, "Получен статус код " + statusCodeGet);
    }

    @Test
    public void newSubtaskTestId () {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        Subtask newSubtask = new Subtask("Тест", "Описание", NEW,0, 1,LocalDateTime.of(2022,5,27,18,00,10), Duration.ofMinutes(15));
        String jsonSubtask = gson.toJson(newSubtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet = response.statusCode();

        Assertions.assertEquals(201, statusCodeGet, "Получен статус код " + statusCodeGet);

        //Тест на GET запрос по Id проверка статус кода 200
        URI url1 = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        HttpResponse<String> response1 = null;
        try {
            response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet1 = response1.statusCode();

        Assertions.assertEquals(200, statusCodeGet1, "Получен статус код " + statusCodeGet1);
        Assertions.assertEquals(gson.toJson(httpTaskServer.manager.getSubtask(1)), response1.body(), "Получено body " + response1.body() );

        //Тест на GET запрос по Id проверка статус кода 400
        URI url2 = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpResponse<String> response2 = null;
        try {
            response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet2 = response2.statusCode();

        Assertions.assertEquals(400, statusCodeGet2, "Получен статус код " + statusCodeGet2);

        //Тест на запрос DELETE по id
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url1)
                .DELETE()
                .build();
        HttpResponse<String> response3 = null;
        try {
            response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet3 = response3.statusCode();
        System.out.println(httpTaskServer.manager.getSubtasks());

        Assertions.assertEquals(200, statusCodeGet3, "Получен статус код " + statusCodeGet3);
        Assertions.assertEquals(null, httpTaskServer.manager.getSubtask(1));

        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(url2)
                .DELETE()
                .build();
        HttpResponse<String> response4 = null;
        try {
            response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet4 = response4.statusCode();

        Assertions.assertEquals(400, statusCodeGet4, "Получен статус код " + statusCodeGet4);
        Assertions.assertNull(httpTaskServer.manager.getSubtask(1));
    }

    @Test
    public void newEpicsTest () {
        //Тест на запрос POST
        HttpClient client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic");
        Epic newEpic = new Epic("Тест", "Описание", NEW, 0);
        String jsonEpic = gson.toJson(newEpic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet = response.statusCode();

        Assertions.assertEquals(201, statusCodeGet, "Получен статус код " + statusCodeGet);

        //Тест на запрос GET
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        HttpResponse<String> response1 = null;
        try {
            response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet1 = response1.statusCode();

        Assertions.assertEquals(200, statusCodeGet1, "Получен статус код " + statusCodeGet1);
        Assertions.assertEquals(gson.toJson(httpTaskServer.manager.getEpics()), response1.body(), "Получено body " + response1.body() );

        //Тест на запрос DELETE
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url1)
                .DELETE()
                .build();
        HttpResponse<String> response2 = null;
        try {
            response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet2 = response2.statusCode();

        Assertions.assertEquals(200, statusCodeGet2, "Получен статус код " + statusCodeGet2);
        Assertions.assertEquals(0, httpTaskServer.manager.getEpics().size());

        //Тест на запрос PUT, а такого запроса нет
        Epic newEpic1 = new Epic("Тест", "Описание", NEW, 0);
        String jsonEpic1 = gson.toJson(newEpic1);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(jsonEpic1);
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url1)
                .PUT(body1)
                .build();
        HttpResponse<String> response3 = null;
        try {
            response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet3 = response3.statusCode();

        Assertions.assertEquals(405, statusCodeGet3, "Получен статус код " + statusCodeGet);
    }

    @Test
    public void newEpicsTestId () {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic newEpic = new Epic("Тест", "Описание", NEW, 0);
        String jsonEpic = gson.toJson(newEpic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet = response.statusCode();

        Assertions.assertEquals(201, statusCodeGet, "Получен статус код " + statusCodeGet);

        //Тест на GET запрос по Id проверка статус кода 200
        URI url1 = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        HttpResponse<String> response1 = null;
        try {
            response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet1 = response1.statusCode();

        Assertions.assertEquals(200, statusCodeGet1, "Получен статус код " + statusCodeGet1);
        Assertions.assertEquals(gson.toJson(httpTaskServer.manager.getEpic(1)), response1.body(), "Получено body " + response1.body() );

        //Тест на GET запрос по Id проверка статус кода 400
        URI url2 = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpResponse<String> response2 = null;
        try {
            response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet2 = response2.statusCode();

        Assertions.assertEquals(400, statusCodeGet2, "Получен статус код " + statusCodeGet2);

        //Тест на запрос DELETE по id
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url1)
                .DELETE()
                .build();
        HttpResponse<String> response3 = null;
        try {
            response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet3 = response3.statusCode();

        Assertions.assertEquals(200, statusCodeGet3, "Получен статус код " + statusCodeGet3);
        Assertions.assertEquals(null, httpTaskServer.manager.getEpic(1));

        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(url1)
                .DELETE()
                .build();
        HttpResponse<String> response4 = null;
        try {
            response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet4 = response4.statusCode();

        Assertions.assertEquals(400, statusCodeGet4, "Получен статус код " + statusCodeGet4);
        Assertions.assertNull(httpTaskServer.manager.getEpic(1));
    }

    @Test
    public void newHistoryTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task");
        Task newTask = new Task("Тест", "Описание", NEW, 0, LocalDateTime.of(2022, 6, 11, 15, 50, 10), Duration.ofMinutes(30));
        String jsonTask = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response1 = null;
        try {
            response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Тест запроса Get
        URI url2 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpResponse<String> response2 = null;
        try {
            response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int statusCodeGet = response2.statusCode();
        Assertions.assertEquals(200,statusCodeGet,"Получен статус код " + statusCodeGet);
        Assertions.assertEquals(1,httpTaskServer.manager.getHistory().size());

        //Тест запроса DELETE, которого нет
        URI url3 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url3)
                .DELETE()
                .build();
        HttpResponse<String> response3 = null;
        try {
            response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int statusCodeGet1 = response3.statusCode();
        Assertions.assertEquals(405,statusCodeGet1,"Получен статус код " + statusCodeGet1);
    }

    @Test
    public void newPriorityTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task");
        Task newTask = new Task("Тест", "Описание", NEW, 0, LocalDateTime.of(2022, 6, 11, 15, 50, 10), Duration.ofMinutes(30));
        String jsonTask = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URI url2 = URI.create("http://localhost:8080/tasks/subtask");
        Subtask newSubtask = new Subtask("Тест", "Описание", NEW,0, 0,LocalDateTime.of(2022,5,27,18,00,10), Duration.ofMinutes(15));
        String jsonSubtask = gson.toJson(newSubtask);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .header("Content-Type", "application/json")
                .POST(body1)
                .build();
        HttpResponse<String> response1 = null;
        try {
            response1 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URI url3 = URI.create("http://localhost:8080/tasks");
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url3)
                .GET()
                .build();
        HttpResponse<String> response2 = null;
        try {
            response2 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int statusCodeGet = response2.statusCode();
        Assertions.assertEquals(200,statusCodeGet,"Получен статус код " + statusCodeGet);

        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(url3)
                .DELETE()
                .build();
        HttpResponse<String> response3 = null;
        try {
            response3 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int statusCodeGet1 = response3.statusCode();
        Assertions.assertEquals(405,statusCodeGet1,"Получен статус код " + statusCodeGet1);
    }
}
