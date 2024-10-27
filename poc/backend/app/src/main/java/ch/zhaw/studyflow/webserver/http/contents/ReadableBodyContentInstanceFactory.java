package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.services.ServiceCollection;

import java.io.InputStream;
import java.util.Map;

/**
 * A factory for creating readable body content instances.
 */
@FunctionalInterface
public interface ReadableBodyContentInstanceFactory {
    /**
     * Creates a new instance of a readable body content.
     * @param mimeType The MIME type of the content.
     * @param serviceCollection The service collection to inject services into the content.
     * @param properties The properties of the content.
     * @param inputStream The input stream of the content.
     * @return The new instance of the readable body content.
     */
    ReadableBodyContent create(String mimeType, ServiceCollection serviceCollection, Map<String, String> properties, InputStream inputStream);
}
