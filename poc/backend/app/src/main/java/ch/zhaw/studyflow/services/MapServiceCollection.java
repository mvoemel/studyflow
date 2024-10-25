package ch.zhaw.studyflow.services;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class MapServiceCollection implements ServiceCollection {
    private final Map<Class<?>, Function<ServiceCollection,?>> services;


    public MapServiceCollection(Map<Class<?>, Function<ServiceCollection,?>> services) {
        this.services = services;
    }


    @Override
    public <T> Optional<T> getService(Class<T> clazz) {
        return getRequiredServiceInternal(clazz);
    }

    @Override
    public <T> T getRequiredService(Class<T> clazz) {
        return getRequiredServiceInternal(clazz)
                .orElseThrow(() -> new IllegalArgumentException("Service not available"));
    }


    private <T> Optional<T> getRequiredServiceInternal(Class<T> clazz) {
        Function<ServiceCollection, ?> factory = services.get(clazz);
        Object service = factory.apply(this);
        if (clazz.isInstance(service)) {
            return Optional.of(clazz.cast(service));
        } else {
            throw new IllegalArgumentException("Service is not of the expected type");
        }
    }
}
