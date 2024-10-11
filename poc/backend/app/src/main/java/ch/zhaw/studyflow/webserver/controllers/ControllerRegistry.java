package ch.zhaw.studyflow.webserver.controllers;

import java.util.List;

public interface ControllerRegistry {
    void registerController(Class<?> controller);
    List<ControllerMetadata> getRegisteredControllers();
}
