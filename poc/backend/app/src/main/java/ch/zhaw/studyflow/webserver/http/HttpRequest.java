package ch.zhaw.studyflow.webserver.http;

import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContent;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;

import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Represents an HTTP request.
 */
public interface HttpRequest {
    /**
     * Returns the cookies of the request.
     * @return the cookies of the request.
     */
    CookieContainer getCookies();

    /**
     * Returns the charset of the request.
     * @return the charset of the request.
     */
    Charset getRequestCharset();

    /**
     * Returns the body of the request.
     * @return the body of the request.
     */
    Optional<ReadableBodyContent> getRequestBody();

    /**
     * Creates a response for the request.
     * @return a response for the request.
     */
    HttpResponse createResponse();
}
