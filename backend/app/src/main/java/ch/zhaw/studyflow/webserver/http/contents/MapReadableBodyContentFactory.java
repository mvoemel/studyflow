package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.services.ServiceCollection;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class MapReadableBodyContentFactory implements ReadableBodyContentFactory {
    private final ServiceCollection services;
    private final Map<String, ReadableBodyContentInstanceFactory> factories;


    public MapReadableBodyContentFactory(ServiceCollection services, Map<String, ReadableBodyContentInstanceFactory> factories) {
        Objects.requireNonNull(services);
        Objects.requireNonNull(factories);

        this.services   = services;
        this.factories  = factories;
    }


    @Override
    public ReadableBodyContent create(String mimeType, Map<String , String> properties, InputStream inputStream) {
        final ReadableBodyContentInstanceFactory factory = factories.get(mimeType);
        if (factory == null) {
            throw new IllegalArgumentException("No factory for mime type: " + mimeType);
        }
        return factory.create(mimeType, services, properties, inputStream);
    }
}
