package ch.zhaw.studyflow.webserver.http.contents;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents a body content that can be read.
 */
public interface ReadableBodyContent extends BodyContent {
    /**
     * Reads the content and returns it as the specified type.
     * @param valueType The type to read the content as.
     * @return The read content.
     * @param <T> The type to read the content as.
     * @throws IOException If an I/O error occurs.
     * @throws IllegalArgumentException If the content cannot be read as the specified type.
     */
    <T> T read(Class<T> valueType) throws IOException;

    /**
     * Tries to read the content and returns it as the specified type.
     * @param valueType The type to read the content as.
     * @return The read content.
     * @param <T> The type to read the content as.
     */
    <T>Optional<T> tryRead(Class<T> valueType);
}
