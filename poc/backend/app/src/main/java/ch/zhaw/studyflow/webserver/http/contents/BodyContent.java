package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

import java.io.InputStream;
import java.io.OutputStream;

public interface BodyContent {
    String getMimeType();

    void writeTo(HttpResponse request, OutputStream output);
    void readFrom(HttpRequest request, InputStream input);
}
