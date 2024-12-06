package ch.zhaw.studyflow.webserver.http.cookies;

/**
 * Represents the SameSite policy of a cookie.
 * For brevity, the descriptions of the policies are quite short. For more information, see the MDN Web Docs.
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie/SameSite">MDN Web Docs</a>
 */
public enum SameSitePolicy {
    /**
     * Cookies are sent only for same-site requests, not for cross-site requests.
     */
    STRICT("Strict"),

    /**
     * Cookies are sent for top-level navigation to the site but not for other cross-site requests.
     */
    LAX("Lax"),

    /**
     * Cookies are sent for both same-site and cross-site requests but require the Secure attribute.
     */
    NONE("None");

    private final String value;

    SameSitePolicy(String value) {
        this.value  = value;
    }

    /**
     * Returns the string value of the policy used in the Set-Cookie header.
     * @return the value of the policy.
     */
    public String getValue() {
        return value;
    }

    /**
     * Tries to parse a SameSite policy from a string.
     * @param value The value to extract the policy from.
     * @return The SameSite policy with the given value or null if the value is not a valid policy.
     */
    public static SameSitePolicy fromString(String value) {
        for (SameSitePolicy policy : SameSitePolicy.values()) {
            if (policy.getValue().equalsIgnoreCase(value)) {
                return policy;
            }
        }
        return null;
    }
}
