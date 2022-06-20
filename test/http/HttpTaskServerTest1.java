package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.DurationAdapter;
import json.LocalDateTimeAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        HttpClient client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task");  //создание и обновление task, если у новой задачи id совпадает со старой задачей, то идет обновление
        Task newTask = new Task("Тест", "Описание", NEW, 0, LocalDateTime.of(2022, 6, 11, 15, 50, 10), Duration.ofMinutes(30));
        String jsonTask = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response1 = null;
        try {
            response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int statusCodeGet = response1.statusCode();

        Assertions.assertEquals(201, statusCodeGet, "Получен статус код " + statusCodeGet);
    }
}
