package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.DurationAdapter;
import json.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;


public class KVClient {
    String token;  //KVServer register
    HttpClient client;
    String url;

    //регистрация
    public KVClient(String url) {
        this.url = url;
        client = HttpClient.newHttpClient();
        URI url1 = URI.create(url + "/register");
        HttpRequest request =HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                if (!response.body().isEmpty()){
                    token = response.body();
                    System.out.println(token);
                }
            } else {
                System.out.println("Что то пошло не так " + response.statusCode());
            }
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }
    }


    //чтение
    String load(String key) { //tasks,epics,subtasks,history
        String json = null;
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + token );
        HttpRequest request =HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                if (!response.body().isEmpty()){
                    json = response.body();
                    System.out.println(json);
                }
            } else {
                System.out.println("Что то пошло не так " + response.statusCode());
                return null;
            }
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }

        return json;
    }

   // запись
    void put(String key, String value) {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + token );
        HttpRequest request =HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        try {
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (NullPointerException | InterruptedException | IOException e) {
            System.out.println("Во время выполнения POST запроса возникла ошибка.\n" + "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}
