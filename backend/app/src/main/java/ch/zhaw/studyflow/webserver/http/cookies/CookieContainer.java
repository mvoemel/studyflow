package ch.zhaw.studyflow.webserver.http.cookies;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents a container for cookies.
 * A cookie container can be used to store and retrieve cookies that are associated with a request or response.
 */
public interface CookieContainer {
    /**
     * Checks if a cookie with the given name is present.
     * @param name The name of the cookie.
     * @return true if a cookie with the given name is present, false otherwise.
     */
    boolean hasCookie(String name);

    /**
     * Returns the cookie with the given name.
     * @param name The name of the cookie.
     * @return The cookie with the given name or an empty optional if no cookie with the given name is present.
     */
    Optional<Cookie> get(String name);

    /**
     * Sets a cookie with the given name and value.
     * @param name The name of the cookie.
     * @param value The value of the cookie.
     */
    void set(String name, String value);

    /**
     * Sets a cookie.
     * @param cookie The cookie to set.
     */
    void set(Cookie cookie);

    /**
     * Removes a cookie with the given name.
     * @param name The name of the cookie to remove.
     */
    void remove(String name);

    /**
     * Returns the cookies as a collection.
     * @return The cookies as a collection.
     */
    Collection<Cookie> asCollection();
}
