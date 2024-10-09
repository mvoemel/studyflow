package ch.zhaw.studyflow.webserver.java;

import ch.zhaw.studyflow.webserver.ControllerFactory;
import ch.zhaw.studyflow.webserver.WebServer;
import ch.zhaw.studyflow.webserver.WebServerBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.List;

public class HttpServerWebServerBuilder implements WebServerBuilder {
    private ControllerFactory controllerFactory;
    private List<Class<?>> controllers;


    private HttpServerWebServerBuilder() {
        this.controllers = new ArrayList<>();
    }


    @Override
    public WebServerBuilder setControllerFactory(ControllerFactory controllerFactory) {
        return null;
    }

    @Override
    public <T> WebServerBuilder addController(Class<T> controller) {
        controllers.add(controller);
        HttpExchange ex;
        return this;
    }

    @Override
    public WebServer build() {
        return new HttpServerWebServer();
    }
}
