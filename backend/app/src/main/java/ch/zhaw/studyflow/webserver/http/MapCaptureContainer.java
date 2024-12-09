package ch.zhaw.studyflow.webserver.http;

import java.util.*;

/**
 * A container for captures.
 */
public class MapCaptureContainer implements CaptureContainer {
    private final Map<String, String> capturesMap;


    public MapCaptureContainer(Map<String, String> captures) {
        this.capturesMap    = Collections.unmodifiableMap(captures);
    }

    @Override
    public int size() {
        return capturesMap.size();
    }

    @Override
    public Collection<String> keys() {
        return capturesMap.keySet();
    }

    @Override
    public Collection<String> values() {
        return capturesMap.values();
    }

    @Override
    public Optional<String> get(final String key) {
        if (capturesMap.containsKey(key)) {
            return Optional.of(capturesMap.get(key));
        }
        return Optional.empty();
    }
}
