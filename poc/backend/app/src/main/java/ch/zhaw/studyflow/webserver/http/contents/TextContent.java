package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

import java.io.*;
import java.util.Objects;

public class TextContent implements BodyContent {
    private String mimeType;
    private String content;

    public TextContent(String content) {
        this("text/plain", content);
    }

    public TextContent(String mimeType, String content) {
        Objects.requireNonNull(mimeType);
        Objects.requireNonNull(content);

        this.mimeType   = mimeType;
        this.content    = content;
    }


    @Override
    public String getMimeType() {
        return mimeType;
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
        content = new BufferedReader(new InputStreamReader(input, request.getResponseCharset())).toString();
    }
}
