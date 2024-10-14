package ch.zhaw.studyflow.webserver;

public interface WebServerBuilder {
    WebServerBuilder setControllerFactory(ControllerFactory controllerFactory);

    <T> WebServerBuilder addController(Class<T> controller);

    WebServer build();
}
