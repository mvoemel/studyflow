package ch.zhaw.studyflow.webserver.cookies;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class HashMapCookieContainer implements CookieContainer {
    private final HashMap<String, Cookie> cookies;


    public HashMapCookieContainer() {
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
