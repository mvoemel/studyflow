package ch.zhaw.studyflow.webserver.http.cookies;

public enum SameSitePolicy {
    STRICT("Strict"),
    LAX("Lax"),
    NONE("None");

    private final String value;

    SameSitePolicy(String value) {
        this.value  = value;
    }

    public String getValue() {
        return value;
    }

    public static SameSitePolicy fromString(String value) {
        for (SameSitePolicy policy : SameSitePolicy.values()) {
            if (policy.getValue().equalsIgnoreCase(value)) {
                return policy;
            }
        }
        return null;
    }
}
