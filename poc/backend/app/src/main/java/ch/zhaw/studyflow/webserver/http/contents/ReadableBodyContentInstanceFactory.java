package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.services.ServiceCollection;

import java.io.InputStream;
import java.util.Map;

/**
 * A factory for creating readable body content instances.
 */
@FunctionalInterface
public interface ReadableBodyContentInstanceFactory {
    ReadableBodyContent create(String mimeType, ServiceCollection serviceCollection, Map<String, String> properties, InputStream inputStream);
}
