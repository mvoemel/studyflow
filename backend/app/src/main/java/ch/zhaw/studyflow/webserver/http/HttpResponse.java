package ch.zhaw.studyflow.webserver.http;

import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;

/**
 * Represents an HTTP response.
 */
public interface HttpResponse {
    /**
     * Returns the {@link CookieContainer} of the response.
     * @return the cookies of the response.
     */
    CookieContainer getCookies();

    /**
     * Returns the status code of the response.
     * @return the status code of the response.
     */
    HttpStatusCode getStatusCode();
    /**
     * Sets the status code of the response.
     * @param statusCode the status code of the response.
     * @return the response.
     */
    HttpResponse setStatusCode(HttpStatusCode statusCode);

    /**
     * Returns the body of the response.
     * @return the body of the response.
     */
    WritableBodyContent getResponseBody();

    /**
     * Sets the body of the response.
     * @param content the body of the response.
     * @return the response.
     */
    HttpResponse setResponseBody(WritableBodyContent content);
}
