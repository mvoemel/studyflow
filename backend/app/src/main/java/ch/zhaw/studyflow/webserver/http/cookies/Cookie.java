package ch.zhaw.studyflow.webserver.http.cookies;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents an HTTP cookie as specified by RFC 6265 (<a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">...</a>).
 */
public final class Cookie {
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT).withLocale(Locale.US);


    private final String name;
    private final String value;
    private boolean httpOnly;
    private long maxAge;
    private boolean secure;
    private String domain;
    private String path;
    private SameSitePolicy sameSite;
    private LocalDateTime expires;


    /**
     * Creates a new instance of Cookie with the specified name and value.
     * @param name The name of the cookie.
     * @param value The value of the cookie.
     */
    public Cookie(String name, String value) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(value, "value must not be null");

        this.name       = name;
        this.value      = value;
        this.path       = "/";
        this.httpOnly   = true;
    }


    /**
     * Gets the name of the cookie.
     * @return The name of the cookie.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the value of the cookie.
     * @return The value of the cookie.
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the maximum age of the cookie.
     * The max age is the number of seconds until the cookie expires.
     * @return The maximum age of the cookie.
     */
    public long getMaxAge() {
        return maxAge;
    }

    /**
     * Sets the maximum age of the cookie.
     * The unit of the max age is seconds.
     * @param maxAge The maximum age of the cookie.
     */
    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    /**
     * Sets the cookie to be HTTP only.
     * @param httpOnly True if the cookie should be HTTP only, false otherwise.
     */
    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    /**
     * Gets whether the cookie is HTTP only.
     * @return True if the cookie is HTTP only, false otherwise.
     */
    public boolean isHttpOnly() {
        return httpOnly;
    }

    /**
     * Gets whether the cookie should only be sent over secure connections.
     * @return True if the cookie is secure, false otherwise.
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * Sets whether the cookie should only be sent over secure connections.
     * @param secure True if the cookie should only be sent over secure connections, false otherwise.
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    /**
     * Gets the domain of the cookie.
     * @return The domain of the cookie.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the domain of the cookie.
     * @param domain The domain of the cookie.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Gets the path of the cookie.
     * @return The path of the cookie.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path of the cookie.
     * @param path The path of the cookie.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the SameSite policy of the cookie.
     * @return The SameSite policy of the cookie.
     */
    public SameSitePolicy getSameSite() {
        return sameSite;
    }

    /**
     * Sets the SameSite policy of the cookie.
     * @param sameSite The SameSite policy of the cookie.
     */
    public void setSameSite(SameSitePolicy sameSite) {
        this.sameSite = sameSite;
    }

    /**
     * Gets the expiration date of the cookie.
     * @return The expiration date of the cookie.
     */
    public LocalDateTime getExpires() {
        return expires;
    }

    /**
     * Sets the expiration date of the cookie.
     * @param expires The expiration date of the cookie.
     */
    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    @Override
    public String toString() {
        return toHeaderFormat();
    }

    /**
     * Converts the cookie to a string that can be used in an HTTP header.
     * @return The cookie in a format that can be used in an HTTP header.
     */
    public String toHeaderFormat() {
        StringBuilder builder = new StringBuilder(name);
        builder.append("=").append(value);

        if (expires != null) {
            builder.append("; Expires=").append(expires.format(DATE_FORMATTER));
        }

        if (maxAge > 0) {
            builder.append("; Max-Age=").append(maxAge);
        }

        if (domain != null) {
            builder.append("; Domain=").append(domain);
        }

        if (path != null) {
            builder.append("; Path=").append(path);
        }

        if (secure) {
            builder.append("; Secure");
        }

        if (httpOnly) {
            builder.append("; HttpOnly");
        }

        if (sameSite != null) {
            builder.append("; SameSite=").append(sameSite.getValue());
        }
        return builder.toString();
    }

    /**
     * Reads a cookie from an HTTP header.
     * @param header The header to read the cookie from.
     * @return The cookie, or an empty optional if the cookie could not be read.
     */
    public static Optional<Cookie> readFromHeader(String header) {
        Objects.requireNonNull(header, "header must not be null");

        String[] parts = header.split(";");
        if (parts.length == 0) {
            return Optional.empty();
        }

        String[] nameValue = parts[0].split("=");
        if (nameValue.length != 2) {
            return Optional.empty();
        }

        Cookie cookie = new Cookie(nameValue[0].trim(), nameValue[1].trim());
        if (parts.length > 1) {
            for (int i = 1; i < parts.length; i++) {
                String[] attribute = parts[i].split("=");
                if (attribute.length == 2) {
                    switch (attribute[0].trim().toLowerCase()) {
                        case "expires" -> cookie.setExpires(LocalDateTime.parse(attribute[1].trim(), DATE_FORMATTER));
                        case "max-age" -> cookie.setMaxAge(Long.parseLong(attribute[1].trim()));
                        case "domain" -> cookie.setDomain(attribute[1].trim());
                        case "path" -> cookie.setPath(attribute[1].trim());
                        case "secure" -> cookie.setSecure(true);
                        case "httponly" -> cookie.setHttpOnly(true);
                        case "samesite" -> cookie.setSameSite(SameSitePolicy.fromString(attribute[1].trim()));
                        default -> { /* intentionally left blank - we ignore unknown attributes */ }
                    }
                }
            }
        }
        return Optional.of(cookie);
    }
}
