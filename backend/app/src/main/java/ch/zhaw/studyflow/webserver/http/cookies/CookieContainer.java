package ch.zhaw.studyflow.webserver.http.cookies;

import java.util.Collection;
import java.util.Optional;

public interface CookieContainer {

    boolean hasCookie(String name);

    Optional<Cookie> get(String name);
    void set(String name, String value);
    void set(Cookie cookie);
    void remove(String name);

    Collection<Cookie> asCollection();
}
