package ch.zhaw.studyflow.webserver.http.contents;

/**
 * Represents a body content that can be either read or written depending on the concrete implementation.
 */
public interface BodyContent {
    /**
     * Returns the content type header.
     * @return The content type header.
     */
    String getContentTypeHeader();

    /**
     * Returns the content length.
     * @return The content length.
     */
    long getContentLength();
}
