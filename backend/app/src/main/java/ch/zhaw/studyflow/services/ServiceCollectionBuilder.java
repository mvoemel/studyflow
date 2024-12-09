package ch.zhaw.studyflow.services;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A builder for a collection of services.
 * A service collection is used to manage the services that are used by the application and provides a way to get
 * a service by its class.
 * Services may provide their own factories if they depend on other services themselves.
 */
public interface ServiceCollectionBuilder {
    /**
     * Registers a service using an unique instance (singelton) that is shared by all consumers.
     * The {@code factory} is called once and the returned value stored and returned on future requests.
     * @param clazz The class of the service
     * @param factory The instance of the service
     * @return The builder
     * @param <T> The type of the service
     */
    <T> ServiceCollectionBuilder registerSingelton(Class<T> clazz, Function<ServiceCollection, T> factory);

    /**
     * Registers a service using a constructor that is called for each consumer.
     * @param clazz The class of the service
     * @param constructor The constructor that creates a new instance of the service
     * @return The builder
     * @param <T> The type of the service
     */
    <T> ServiceCollectionBuilder register(Class<T> clazz, Supplier<T> constructor);

    /**
     * Registers a service using a factory that is called for each consumer.
     * The factory will receive this service collection as an argument.
     * @param clazz The class of the service
     * @param factory The factory that creates a new instance of the service
     * @return The builder
     * @param <T> The type of the service
     */
    <T> ServiceCollectionBuilder register(Class<T> clazz, Function<ServiceCollection, T> factory);

    /**
     * Builds the service collection.
     * @return The service collection
     */
    ServiceCollection build();
}
