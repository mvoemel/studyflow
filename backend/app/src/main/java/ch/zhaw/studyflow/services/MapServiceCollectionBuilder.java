package ch.zhaw.studyflow.services;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * An implementation of the {@link ServiceCollectionBuilder} that uses a map to store the services.
 */
public class MapServiceCollectionBuilder implements ServiceCollectionBuilder {
    private static final Logger LOGGER = Logger.getLogger(MapServiceCollectionBuilder.class.getName());

    private final Map<Class<?>, Function<ServiceCollection, ?>> services;


    public MapServiceCollectionBuilder() {
        this.services = new HashMap<>();
    }


    @Override
    public <T> ServiceCollectionBuilder registerSingelton(Class<T> clazz, Function<ServiceCollection, T> factory) {
        /*
         * Creates a memoized factory that only creates the instance once.
         * To do so we use the "memoization" pattern, we capture the factory and the instance in a closure
         * and return it if it is already created; otherwise, we create the instance and store it in the closure
         * and return it.
         */
        registerServiceInternally(clazz, createMemoizedFactory(factory));
        return this;
    }

    @Override
    public <T> ServiceCollectionBuilder register(Class<T> clazz, Supplier<T> constructor) {
        registerServiceInternally(clazz,serviceCollection -> constructor.get());
        return this;
    }

    @Override
    public <T> ServiceCollectionBuilder register(Class<T> clazz, Function<ServiceCollection, T> factory) {
        registerServiceInternally(clazz, factory);
        return this;
    }

    @Override
    public ServiceCollection build() {
        return new MapServiceCollection(services);
    }

    
    private <T> void registerServiceInternally(Class<T> clazz, Function<ServiceCollection, T> factory) {
        if (services.containsKey(clazz)) {
            LOGGER.warning(() -> "Service '%s' registered twice. Ignoring second registration.".formatted(clazz.getName()));
            return;
        }
        this.services.put(clazz, factory);
    }

    /**
     * Creates a memoized factory that only creates the instance once.
     * This method is used for the registration of singletons.
     * @param factory The factory to create the instance.
     * @return The memoized factory.
     * @param <T> The type of the instance.
     */
    private <T> Function<ServiceCollection, T> createMemoizedFactory(Function<ServiceCollection, T> factory) {
        return new Function<>() {
            private T instance = null;

            @Override
            public T apply(ServiceCollection serviceCollection) {
                if (instance == null) {
                    instance = factory.apply(serviceCollection);
                }
                return instance;
            }
        };
    }
}
