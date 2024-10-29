package ch.zhaw.studyflow.webserver.http.contents;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents a body content that can be written to an output stream.
 */
public interface WritableBodyContent extends BodyContent {
    /**
     * Writes the content to the output stream.
     *
     * @param outputStream The output stream to write to.
     * @throws IOException If an I/O error occurs.
     */
    void write(OutputStream outputStream) throws IOException;
}
