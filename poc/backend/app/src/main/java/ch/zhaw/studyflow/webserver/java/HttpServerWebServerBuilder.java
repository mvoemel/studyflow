package ch.zhaw.studyflow.webserver.java;

import ch.zhaw.studyflow.webserver.ControllerFactory;
import ch.zhaw.studyflow.webserver.WebServer;
import ch.zhaw.studyflow.webserver.WebServerBuilder;
import ch.zhaw.studyflow.webserver.controllers.ControllerRegistry;
import ch.zhaw.studyflow.webserver.controllers.SimpleControllerRegistry;
import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.List;

public class HttpServerWebServerBuilder implements WebServerBuilder {
    private ControllerRegistry controllerRegistry;
    private List<Class<?>> controllers;


    private HttpServerWebServerBuilder() {
        this.controllers        = new ArrayList<>();
        this.controllerRegistry = new SimpleControllerRegistry();
    }


    @Override
    public WebServerBuilder setControllerFactory(ControllerFactory controllerFactory) {
        return null;
    }

    @Override
    public <T> WebServerBuilder addController(Class<T> controller) {
        this.controllers.add(controller);
        return this;
    }

    @Override
    public WebServer build() {

        return new HttpServerWebServer();
    }
}
