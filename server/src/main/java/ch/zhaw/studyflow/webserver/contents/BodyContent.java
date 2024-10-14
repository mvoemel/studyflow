package ch.zhaw.studyflow.webserver.contents;

import ch.zhaw.studyflow.webserver.HttpRequest;
import ch.zhaw.studyflow.webserver.HttpResponse;

import java.io.InputStream;
import java.io.OutputStream;

public interface BodyContent {
    String getMimeType();

    void writeTo(HttpResponse request, OutputStream output);
    void readFrom(HttpRequest request, InputStream input);
}
