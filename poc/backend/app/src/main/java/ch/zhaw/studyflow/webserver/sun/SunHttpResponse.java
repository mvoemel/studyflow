package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.BodyContent;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;
import ch.zhaw.studyflow.webserver.http.cookies.HashMapCookieContainer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SunHttpResponse implements HttpResponse {
    private final SunHttpRequest request;
    private BodyContent responseContent;
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
    public HttpResponse setResponseContent(BodyContent content) {
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
    public Charset getResponseCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public BodyContent getResponseContent() {
        return responseContent;
    }
}
