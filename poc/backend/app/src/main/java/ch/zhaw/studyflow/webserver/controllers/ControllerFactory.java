package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.services.ServiceCollection;

/**
 * A factory for a specific controller type.
 * @param <T> The type of the controller.
 */
@FunctionalInterface
public interface ControllerFactory<T> {
    /**
     * Creates a new instance of the controller.
     * @param serviceCollection The service collection to inject into the controller.
     * @return The new instance of the controller.
     */
    T create(ServiceCollection serviceCollection);
}
