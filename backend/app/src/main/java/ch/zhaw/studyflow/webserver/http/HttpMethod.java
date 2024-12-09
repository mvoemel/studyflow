package ch.zhaw.studyflow.webserver.http;

/**
 * An enumeration of HTTP methods that can be used in a request.
 */
public enum HttpMethod {
    /**
     * Represents the GET method of a request.
     */
    GET("GET"),

    /**
     * Represents the PUT method of a request.
     */
    PUT("PUT"),

    /**
     * Represents the POST method of a request.
     */
    POST("POST"),

    /**
     * Represents the DELETE method of a request.
     */
    DELETE("DELETE"),

    /**
     * Represents the PATCH method of a request.
     */
    PATCH("PATCH");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    /**
     * Returns the name of the method.
     * @return the name of the method
     */
    public String methodName() {
        return method;
    }
}
