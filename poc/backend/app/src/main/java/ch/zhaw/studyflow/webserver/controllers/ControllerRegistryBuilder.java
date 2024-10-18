package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.services.ServiceCollection;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A builder for a controller registry.
 * The builder allows to register controllers and build a registry from it.
 */
public interface ControllerRegistryBuilder {
    /**
     * Registers a controller with a supplier (e.g. a constructor) that creates a new instance of the controller for each consumer.
     * @param clazz The class of the controller
     * @param supplier The supplier that creates a new instance of the controller
     * @return The builder
     * @param <C> The type of the controller
     * @throws NullPointerException if clazz or supplier is null
     */
    <C> ControllerRegistryBuilder register(Class<C> clazz, Supplier<C> supplier);

    /**
     * Registers a controller with a factory that creates a new instance of the controller for each consumer.
     * @param clazz The class of the controller
     * @param factory The factory that creates a new instance of the controller
     * @return The builder
     * @param <C> The type of the controller
     * @throws NullPointerException if clazz or factory is null
     */
    <C> ControllerRegistryBuilder register(Class<C> clazz, Function<ServiceCollection, C> factory);

    /**
     * Builds the controller registry.
     * @return The controller registry
     */
    ControllerRegistry build();
}
