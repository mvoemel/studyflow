package ch.zhaw.studyflow.webserver;

import ch.zhaw.studyflow.webserver.controllers.ControllerFactory;

public interface WebServerBuilder {
    WebServerBuilder setControllerFactory(ControllerFactory controllerFactory);

    <T> WebServerBuilder addController(Class<T> controller);

    WebServer build();
}
