package ch.zhaw.studyflow.webserver.http.cookies;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents an HTTP cookie as specified by RFC 6265 (https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie).
 *
 */
public final class Cookie {
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
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

        this.name       = name;
        this.value      = value;
        this.path       = "/";
        this.httpOnly   = true;
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

    @Override
    public String toString() {
        return toHeaderFormat();
    }

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
