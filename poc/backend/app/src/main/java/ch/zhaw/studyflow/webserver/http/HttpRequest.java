package ch.zhaw.studyflow.webserver.http;

import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;

import java.nio.charset.Charset;

public interface HttpRequest {
    CookieContainer getCookies();
    Charset getRequestCharset();

    HttpResponse createResponse();
}
