package http;

import manager.FileBackedTasksManager;

import java.io.IOException;
import java.net.URI;

public class HttpTaskManager extends FileBackedTasksManager {
    int port;
//    URI url = URI.create("http://localhost:8078/");
    KVClient kvClient;
    public HttpTaskManager(int port) throws IOException, InterruptedException {
        this.port = port; //PORT KVServer
        kvClient = new KVClient();
    }

    @Override
    protected void save() {
        kvClient.save();
        super.save();
    }

    @Override
    protected void load() {
        super.load();
    }
}
