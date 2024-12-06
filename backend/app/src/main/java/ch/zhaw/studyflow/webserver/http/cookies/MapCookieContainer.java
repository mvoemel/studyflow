package ch.zhaw.studyflow.webserver.http.cookies;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link CookieContainer} implementation that is backed by a map.
 */
public class MapCookieContainer implements CookieContainer {
    private final Map<String, Cookie> cookies;


    /**
     * Creates a new cookie container with no cookies.
     */
    public MapCookieContainer() {
        this.cookies = new HashMap<>();
    }


    @Override
    public Collection<Cookie> asCollection() {
        return cookies.values();
    }

    @Override
    public boolean hasCookie(String name) {
        return cookies.containsKey(name);
    }

    @Override
    public Optional<Cookie> get(String name) {
        return Optional.ofNullable(cookies.get(name));
    }

    @Override
    public void set(String name, String value) {
        cookies.put(name, new Cookie(name, value));
    }

    @Override
    public void set(Cookie cookie) {
        cookies.put(cookie.getName(), cookie);
    }

    @Override
    public void remove(String name) {
        cookies.remove(name);
    }
}
