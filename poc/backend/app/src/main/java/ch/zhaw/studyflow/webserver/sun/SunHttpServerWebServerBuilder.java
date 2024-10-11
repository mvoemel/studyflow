package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.ControllerFactory;
import ch.zhaw.studyflow.webserver.WebServer;
import ch.zhaw.studyflow.webserver.WebServerBuilder;
import ch.zhaw.studyflow.webserver.controllers.ControllerRegistry;
import ch.zhaw.studyflow.webserver.controllers.SimpleControllerRegistry;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class SunHttpServerWebServerBuilder implements WebServerBuilder {
    private ControllerRegistry controllerRegistry;
    private List<Class<?>> controllers;


    public SunHttpServerWebServerBuilder(InetSocketAddress address, ControllerRegistry registry) {
        this.controllers        = new ArrayList<>();
        this.controllerRegistry = registry;
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

        return new SunHttpServerWebServer();
    }


    public static WebServerBuilder create(InetSocketAddress address) {
        return new SunHttpServerWebServerBuilder(address, new SimpleControllerRegistry());
    }
}
