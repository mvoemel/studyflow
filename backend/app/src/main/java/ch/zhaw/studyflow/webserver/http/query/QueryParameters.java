package ch.zhaw.studyflow.webserver.http.query;

import java.util.*;

public interface QueryParameters {
    /**
     * Returns all available keys.
     */
    Set<String> keys();

    /**
     * Returns a single value if available; otherwise, the optional is not present.
     * If there is more than one value associated with the given key, an exception is thrown.
     */
    Optional<String> getSingleValue(String key);

    /**
     * Returns the values associated with a given key if available; otherwise, the optional is not present.
     */
    Optional<List<String>> getValues(String key);
}