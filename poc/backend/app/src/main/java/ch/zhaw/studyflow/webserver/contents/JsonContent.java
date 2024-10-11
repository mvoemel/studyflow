package ch.zhaw.studyflow.webserver.contents;

import ch.zhaw.studyflow.webserver.HttpRequest;
import ch.zhaw.studyflow.webserver.HttpResponse;

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
    public void writeTo(HttpResponse request, OutputStream output) {

    }

    @Override
    public void readFrom(HttpRequest request, InputStream input) {

    }
}
