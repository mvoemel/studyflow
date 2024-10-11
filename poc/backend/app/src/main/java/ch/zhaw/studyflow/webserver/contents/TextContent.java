package ch.zhaw.studyflow.webserver.contents;

import ch.zhaw.studyflow.webserver.HttpRequest;
import ch.zhaw.studyflow.webserver.HttpResponse;

import java.io.*;

public final class TextContent implements BodyContent {
    private String content;


    public TextContent(String content) {
        this.content = content;
    }


    @Override
    public String getMimeType() {
        return "application/plain";
    }

    @Override
    public void writeTo(HttpResponse request, OutputStream output) {
        OutputStreamWriter writer = new OutputStreamWriter(output, request.getRequestCharset());
        try {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readFrom(HttpRequest request, InputStream input) {
        content = new BufferedReader(new InputStreamReader(input, request.getRequestCharset())).toString();
    }
}
