package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;
import ch.zhaw.studyflow.webserver.http.cookies.HashMapCookieContainer;

public class SunHttpResponse implements HttpResponse {
    private final SunHttpRequest request;
    private WritableBodyContent responseContent;
    private HttpStatusCode statusCode;
    private CookieContainer cookieContainer;

    public SunHttpResponse(SunHttpRequest request) {
        this.request = request;
    }

    @Override
    public CookieContainer getCookies() {
        if (cookieContainer == null) {
            cookieContainer = new HashMapCookieContainer();
        }
        return cookieContainer;
    }

    @Override
    public HttpResponse setResponseBody(WritableBodyContent content) {
        this.responseContent = content;
        return this;
    }

    @Override
    public HttpResponse setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    @Override
    public WritableBodyContent getResponseBody() {
        return responseContent;
    }
}
