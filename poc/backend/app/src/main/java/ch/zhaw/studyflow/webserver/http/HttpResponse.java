package ch.zhaw.studyflow.webserver.http;

import ch.zhaw.studyflow.webserver.http.contents.BodyContent;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;

import java.nio.charset.Charset;

public interface HttpResponse {
    CookieContainer getCookies();
    HttpResponse setResponseContent(BodyContent content);

    HttpResponse setStatusCode(HttpStatusCode statusCode);
    HttpStatusCode getStatusCode();
    Charset getResponseCharset();
    BodyContent getResponseContent();
}
