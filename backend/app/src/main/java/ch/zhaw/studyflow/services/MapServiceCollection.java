package ch.zhaw.studyflow.services;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link ServiceCollection} implementation that is backed by a map.
 */
public class MapServiceCollection implements ServiceCollection {
    private final Map<Class<?>, Function<ServiceCollection,?>> services;


    public MapServiceCollection(Map<Class<?>, Function<ServiceCollection,?>> services) {
        this.services = services;
    }



    @Override
    public <T> Optional<T> getService(Class<T> clazz) {
        return getServiceInternal(clazz);
    }

    @Override
    public <T> T getRequiredService(Class<T> clazz) {
        return getServiceInternal(clazz)
                .orElseThrow(() -> new IllegalArgumentException("Service not available"));
    }


    private <T> Optional<T> getServiceInternal(Class<T> clazz) {
        Optional<T> result = Optional.empty();
        Function<ServiceCollection, ?> factory = services.get(clazz);
        if (factory != null) {
            Object service = factory.apply(this);
            if (!clazz.isInstance(service)) {
                throw new IllegalArgumentException("Service is not of the expected type");
            }
            result = Optional.of(clazz.cast(service));
        }
        return result;
    }
}
