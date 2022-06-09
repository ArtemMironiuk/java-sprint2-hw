package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class KVClient {
    String token;  //KVServer register
    HttpClient client;

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


    String load(String key) throws IOException, InterruptedException { //tasks,epics,subtasks,history
        URI url = URI.create("http://localhost:8078/load/{" + key + "}?API_TOKEN=" + token );
        HttpRequest request =HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            if (!response.body().isEmpty()){
                response.body();
                System.out.println(response.body());
            }
        } else {
            System.out.println("Что то пошло не так " + response.statusCode());
        }
        return response.body();
    }

    void save(String key, String value) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8078/save/{" + key + "}?API_TOKEN=" + token );
        HttpRequest request =HttpRequest.newBuilder()
                .uri(url)
                .POST()
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
    }
}
