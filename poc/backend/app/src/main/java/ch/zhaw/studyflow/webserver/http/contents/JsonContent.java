package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

import java.io.InputStream;
import java.io.OutputStream;

public final class JsonContent<T> implements BodyContent {
    private final Class<T> type;
    private T value;


    public JsonContent(Class<T> type) {
        this.type = type;
    }


    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public void writeTo(HttpResponse request, OutputStream output) {
    }

    @Override
    public void readFrom(HttpRequest request, InputStream input) {
    }
}
