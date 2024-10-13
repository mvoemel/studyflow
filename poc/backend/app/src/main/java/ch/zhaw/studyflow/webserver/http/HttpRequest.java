package ch.zhaw.studyflow.webserver.http;

import java.nio.charset.Charset;

public interface HttpRequest {
    Charset getResponseCharset();

    HttpResponse createResponse();
}
