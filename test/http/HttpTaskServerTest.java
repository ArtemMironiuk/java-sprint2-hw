package http;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static utils.Status.DONE;
import static utils.Status.NEW;

class HttpTaskServerTest extends HttpTaskServer {
    protected HttpTaskServer server;

    public HttpTaskServerTest() throws IOException, InterruptedException {
    }

    @Test
    void handlerTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void handlerTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/task");  //получение списка всех task
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        List<Task> task = getGson().fromJson(response, Task.class);
//
//        assertArrayEquals(server.manager.getTasks(),);

        URI url1 = URI.create("http://localhost:8080/tasks/task");  //создание и обновление task, если у новой задачи id совпадает со старой задачей, то идет обновление
        Gson gson = new Gson();
        Task newTask = new Task("Тест", "Описание", NEW,0, LocalDateTime.of(2022,6,11,15,50,10), Duration.ofMinutes(30));
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body).build();
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/task");  //удаление из списка всех task
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());


    }

    @Test
    void handlerIdTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void handlerSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/subtask");  //получение списка всех subtask
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/subtask");  //создание и обновление subtask, если у новой задачи id совпадает со старой задачей, то идет обновление
        Gson gson = new Gson();
        Subtask newSubtask = new Subtask("Тест", "Описание", NEW,0,0,LocalDateTime.of(2022,6,12,16,50,10), Duration.ofMinutes(50));
        String json = gson.toJson(newSubtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/subtask");  //удаление из списка всех subtask
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void handlerIdSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void handlerEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/epic");  //получение списка всех epic
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/epic");  //создание и обновление epic, если у новой задачи id совпадает со старой задачей, то идет обновление
        Gson gson = new Gson();
        Subtask newSubtask = new Subtask("Тест", "Описание", DONE,0,0,LocalDateTime.of(2022,6,12,22,37,10), Duration.ofMinutes(50));
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(newSubtask);
        Epic newEpic = new Epic("Тест", "Описание", NEW, 0,subtaskList);
        String json = gson.toJson(newEpic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/epic");  //удаление из списка всех epic
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void handlerIdEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void handlerHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void handlerPrioritized() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/tasks/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    }
}