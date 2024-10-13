package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

import java.io.InputStream;
import java.io.OutputStream;

public interface BodyContent {
    /**
     * Returns the MIME type of the content.
     * @return The MIME type.
     */
    String getMimeType();

    /**
     * Returns the length of the content to be sent if possible; otherwise {@code 0} should be returned.
     * If no response body is present, preferably {@code -1} is to be returned to indicate an empty body.
     * Though {@code 0} is also a valid return value in the case of an empty body.
     * @return The length of the content.
     */
    long getContentLength();

    /**
     * Writes the content to the {@code output} stream.
     * @param response The response object.
     * @param output The stream to write to.
     */
    void writeTo(HttpResponse response, OutputStream output);

    /**
     * Reads te content from the {@code input} stream.s
     * @param request The request object.
     * @param input The stream to read from.
     */
    void readFrom(HttpRequest request, InputStream input);
}
