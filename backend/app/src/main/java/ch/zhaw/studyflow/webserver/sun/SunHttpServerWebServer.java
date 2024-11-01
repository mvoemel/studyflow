package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.services.ServiceCollection;
import ch.zhaw.studyflow.webserver.WebServer;
import ch.zhaw.studyflow.webserver.controllers.ControllerRegistry;
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
    private final ServiceCollection serviceCollection;
    private final ControllerRegistry controllerRegistry;
    private final RouteTrie routeTrie;
    private final RequestProcessor invoker;
    private HttpServer server;

    public SunHttpServerWebServer(InetSocketAddress address, ServiceCollection serviceCollection, ControllerRegistry controllerRegistry, RouteTrie routeTrie, RequestProcessor endpointInvoker) {
        this.address            = address;
        this.serviceCollection  = serviceCollection;
        this.controllerRegistry = controllerRegistry;
        this.routeTrie          = routeTrie;
        this.invoker            = endpointInvoker;
    }


    @Override
    public void start() {
        logger.info("Starting simple web server...");
        try {
            logger.fine(() -> "Trying to bind server to '%s'".formatted(address));
            server = HttpServer.create(address, 0);
            logger.info(() -> "Server is running on '%s'".formatted(address));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create server", e);
        }

        HttpHandler requestHandler = new SunRootHttpHandler(serviceCollection, routeTrie, invoker);
        server.createContext("/", requestHandler);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();
    }
}
