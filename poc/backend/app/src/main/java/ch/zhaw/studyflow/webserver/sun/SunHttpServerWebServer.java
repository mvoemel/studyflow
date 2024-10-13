package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.WebServer;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestProcessor;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SunHttpServerWebServer implements WebServer {
    private static final Logger logger = Logger.getLogger(SunHttpServerWebServer.class.getName());

    private final InetSocketAddress address;
    private final RouteTrie routeTrie;
    private final RequestProcessor invoker;
    private HttpServer server;

    public SunHttpServerWebServer(InetSocketAddress address, RouteTrie routeTrie, RequestProcessor endpointInvoker) {
        this.address    = address;
        this.routeTrie  = routeTrie;
        this.invoker    = endpointInvoker;
    }


    @Override
    public void start() {
        logger.info("Starting simple web server...");
        try {
            logger.info(() -> "Server started on {0}" + address);
            server = HttpServer.create(address, 0);
            logger.info(() -> "Server successfully created and bound.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create server", e);
            throw new RuntimeException(e);
        }

        HttpHandler requestHandler = new SunRootHttpHandler(routeTrie, invoker);
        server.createContext("/", requestHandler);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();
    }
}
