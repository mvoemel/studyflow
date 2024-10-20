package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Objects;

public class TextContent extends TextBasedContent {
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private String content;

    public TextContent(String content) {
        this(MIME_TEXT_PLAIN, content);
    }

    public TextContent(String content, Charset charset) {
        this(MIME_TEXT_PLAIN, content, charset);
    }

    public TextContent(String mimeType, String content) {
        super(mimeType);
        this.content    = content;
    }

    public TextContent(String mimeType, String content, Charset charset) {
        super(charset, mimeType);
        Objects.requireNonNull(content);

        this.content    = content;
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public void writeTo(HttpResponse response, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output, response.getResponseCharset());
        writer.write(content);
        writer.flush();
        writer.close();
    }

    @Override
    public void readFrom(HttpRequest request, InputStream input) throws IOException {
        content = new BufferedReader(new InputStreamReader(input, request.getRequestCharset())).toString();
    }
}
