package ch.zhaw.studyflow.webserver.controllers;

public interface ControllerFactory {
    <T> T createController(Class<T> controllerClass);
}
