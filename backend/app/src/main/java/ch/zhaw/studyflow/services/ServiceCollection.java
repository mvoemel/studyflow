package ch.zhaw.studyflow.services;

import java.util.Optional;

/**
 * The service collection contains all services that have been registered during the startup of the application.
 * Services can be retrieved by their class.
 */
public interface ServiceCollection {
    /**
     * Get a service by its class
     * If the service is not available, an empty optional is returned
     * @param clazz The class of the service
     * @return The service if available; otherwise an empty optional.
     * @param <T> The type of the service.
     * @throws NullPointerException If the class is null
     */
    <T> Optional<T> getService(Class<T> clazz);

    /**
     * Get a service by its class or throw an exception if the service is not available
     * @param clazz The class of the service
     * @return The service
     * @param <T> The type of the service
     * @throws NullPointerException If the class is null
     * @throws IllegalArgumentException If the service is not available
     */
    <T> T getRequiredService(Class<T> clazz);
}
