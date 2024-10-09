package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.WebServer;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class SunHttpServer implements WebServer {
    private HttpServer server;

    @Override
    public void start() {
        try {
            server = HttpServer.create();
            server.createContext()
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
