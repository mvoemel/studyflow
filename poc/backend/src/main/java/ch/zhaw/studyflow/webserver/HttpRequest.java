package ch.zhaw.studyflow.webserver;

import java.nio.charset.Charset;

public interface HttpRequest {
    Charset getRequestCharset();

    HttpResponse createResponse();
}
