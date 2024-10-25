package ch.zhaw.studyflow.webserver.http;

import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;

public interface HttpResponse {
    CookieContainer getCookies();

    HttpStatusCode getStatusCode();
    HttpResponse setStatusCode(HttpStatusCode statusCode);

    WritableBodyContent getResponseBody();
    HttpResponse setResponseBody(WritableBodyContent content);
}
