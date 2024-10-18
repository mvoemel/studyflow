package ch.zhaw.studyflow.webserver.controllers;

import java.util.List;

/**
 * A registry for controller containing metadata about all registered controllers.
 */
public interface ControllerRegistry {
    /**
     * Returns a list of all registered controllers.
     * @return a list of all registered controllers
     */
    List<ControllerMetadata> getRegisteredControllers();
}
