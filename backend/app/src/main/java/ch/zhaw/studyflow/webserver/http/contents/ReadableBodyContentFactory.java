package ch.zhaw.studyflow.webserver.http.contents;

import java.io.InputStream;
import java.util.Map;

/**
 * A registry for readable body content factories.
 * This registry is used to create readable body content from a mime type, properties and an input stream.
 */
public interface ReadableBodyContentFactory {
    /**
     * Create a readable body content from the given mime type, properties and input stream.
     * If no factory is available for the given mime type, an IllegalArgumentException is thrown.
     * @param mimeType The mime type of the content
     * @param properties The properties of the content
     * @param inputStream The input stream of the content
     * @return The readable body content
     * @throws IllegalArgumentException If no factory is available for the given mime type
     */
    ReadableBodyContent create(String mimeType, Map<String, String> properties, InputStream inputStream);
}
