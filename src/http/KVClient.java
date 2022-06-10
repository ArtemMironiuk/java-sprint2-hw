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

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

    //регистрация
    public KVClient() throws IOException, InterruptedException {
        KVServer server = new KVServer();
        server.start();
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8078/register");
        HttpRequest request =HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            if (!response.body().isEmpty()){
                token = response.body();
                System.out.println(token);
            }
        } else {
            System.out.println("Что то пошло не так " + response.statusCode());
        }
    }


    //чтение
    String load(String key) throws IOException, InterruptedException { //tasks,epics,subtasks,history
        final  Gson gson = getGson();
        String json = null;
        URI url = URI.create("http://localhost:8078/load/{" + key + "}?API_TOKEN=" + token );
        HttpRequest request =HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            if (!response.body().isEmpty()){
                response.body();
                json = gson.toJson(response.body());

                System.out.println(json);
            }
        } else {
            System.out.println("Что то пошло не так " + response.statusCode());
            return null;
        }
        return json;
    }

    //запись
//    void save(String key, String value) throws IOException, InterruptedException {
//        final
//        URI url = URI.create("http://localhost:8078/save/{" + key + "}?API_TOKEN=" + token );
//        HttpRequest request =HttpRequest.newBuilder()
//                .uri(url)
//                .POST()
//                .build();
//        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
//    }
}
