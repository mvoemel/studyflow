package ch.zhaw.studyflow.webserver.http;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents a container for captures.
 */
public interface CaptureContainer {
    /**
     * Returns the number of captures.
     * @return the number of captures.
     */
    int size();

    /**
     * Returns the keys of the captures.
     * @return the keys of the captures.
     */
    Collection<String> keys();

    /**
     * Returns the values of the captures.
     * @return the values of the captures.
     */
    Collection<String> values();

    /**
     * Returns the value of the capture with the given key.
     * @param key the key of the capture.
     * @return the value of the capture.
     */
    Optional<String> get(final String key);
}
