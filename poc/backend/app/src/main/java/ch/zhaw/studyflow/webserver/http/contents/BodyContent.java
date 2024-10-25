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

    /**
     * Casts the body content to the specified readable body content type.
     * This method is thought as a convenience method to avoid explicit casting and to be used in a fluent way.
     * @param type The type to cast to.
     * @return The casted, readable body content.
     * @param <T> The type to cast to.
     */
    default <T extends ReadableBodyContent> T as(Class<T> type) {
        return type.cast(this);
    }
}
