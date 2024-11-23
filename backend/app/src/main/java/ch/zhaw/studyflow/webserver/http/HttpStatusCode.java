package ch.zhaw.studyflow.webserver.http;

/**
 * An enumeration of HTTP status codes.
 * Use this enumeration to represent the status code of an HTTP response.
 * For more information about status codes see @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Status">MDN Web Docs</a>
 */
public enum HttpStatusCode {
    /**
     * Indicates that the request has succeeded.
     */
    OK(200),

    /**
     * Indicates that the request has been fulfilled and has resulted in one or more new resources being created.
     */
    CREATED(201),

    /**
     * Indicates that the request could not be understood by the server due to malformed syntax.
     */
    BAD_REQUEST(400),

    /**
     * Indicates that the request has not been applied because it lacks valid authentication credentials for the target resource.
     */
    UNAUTHORIZED(401),

    /**
     * Indicates that the server understood the request, but refuses to authorize it.
     */
    FORBIDDEN(403),

    /**
     * Indicates that the server has not found anything matching the Request-URI.
     */
    NOT_FOUND(404),

    /**
     * Indicates that the server cannot or will not process the request due to something that is perceived to be a client error.
     */
    UNSUPPORTED_MEDIA_TYPE(415),

    /**
     * Indicates that the server is a teapot.
     */
    IM_A_TEAPOT(418),

    /**
     * Indicates that the server encountered an unexpected condition which prevented it from fulfilling the request.
     */
    INTERNAL_SERVER_ERROR(500),

    /**
     * Indicates that the server successfully processed the request and is not returning any content.
     */
    NO_CONTENT(204);



    private final int code;

    HttpStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
