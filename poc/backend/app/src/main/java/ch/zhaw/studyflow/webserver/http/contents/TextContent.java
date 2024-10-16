package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TextContent implements BodyContent {
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private String mimeType;
    private Charset charset;
    private String content;

    public TextContent(String content) {
        this(MIME_TEXT_PLAIN, content);
    }

    public TextContent(String content, Charset charset) {
        this(MIME_TEXT_PLAIN, content, charset);
    }

    public TextContent(String mimeType, String content) {
        this(mimeType, content, StandardCharsets.ISO_8859_1);
    }

    public TextContent(String mimeType, String content, Charset charset) {
        Objects.requireNonNull(mimeType);
        Objects.requireNonNull(content);
        Objects.requireNonNull(charset);

        this.mimeType   = mimeType;
        this.content    = content;
        this.charset    = charset;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getContentHeader() {
        return "Content-Type: " + mimeType + (charset != null ? "; charset=" + charset.name() : "");
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public void writeTo(HttpResponse response, OutputStream output) {
        OutputStreamWriter writer = new OutputStreamWriter(output, response.getResponseCharset());
        try {
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readFrom(HttpRequest request, InputStream input) {
        content = new BufferedReader(new InputStreamReader(input, request.getRequestCharset())).toString();
    }
}
