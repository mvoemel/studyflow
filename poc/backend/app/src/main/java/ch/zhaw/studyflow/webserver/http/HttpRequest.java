package ch.zhaw.studyflow.webserver.http;

import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContent;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;

import java.nio.charset.Charset;
import java.util.Optional;

public interface HttpRequest {
    CookieContainer getCookies();
    Charset getRequestCharset();

    Optional<ReadableBodyContent> getRequestBody();

    HttpResponse createResponse();
}
