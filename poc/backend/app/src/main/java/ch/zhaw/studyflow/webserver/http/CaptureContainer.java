package ch.zhaw.studyflow.webserver.http;

import java.util.Collection;
import java.util.Optional;

public interface CaptureContainer {
    int size();
    Collection<String> keys();
    Collection<String> values();

    Optional<String> get(final String key);
}
