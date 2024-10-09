package ch.zhaw.studyflow.webserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a HTTP cookie as specified by RFC 6265 (https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie).
 *
 */
public class Cookie {
    private static final String DATE_FORMAT = "E, dd MMM yyyy HH:mm:ss 'GMT'";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final String name;
    private final String value;
    private boolean httpOnly;
    private long maxAge;
    private boolean secure;
    private String domain;
    private String path;
    private SameSitePolicy sameSite;
    private LocalDateTime expires;


    public Cookie(String name, String value) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(value, "value must not be null");

        this.name   = name;
        this.value  = value;
    }


    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SameSitePolicy getSameSite() {
        return sameSite;
    }

    public void setSameSite(SameSitePolicy sameSite) {
        this.sameSite = sameSite;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public String formatToHeader() {
        StringBuilder builder = new StringBuilder("Set-Cookie: ");
        builder.append(name).append("=").append(value);

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
}
