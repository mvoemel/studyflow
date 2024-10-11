package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.contents.BodyContent;

import java.nio.charset.Charset;

public class SunHttpResponse implements HttpResponse {
    private final SunHttpRequest request;
    private HttpStatusCode responseCode;

    public SunHttpResponse(SunHttpRequest request) {
        this.request = request;
    }

    @Override
    public HttpResponse setResponseContent(BodyContent content) {
    }

    @Override
    public HttpResponse setStatusCode(HttpStatusCode i) {
        return null;
    }

    @Override
    public BodyContent getResponseContent() {
        return null;
    }

    @Override
    public Charset getRequestCharset() {
        return null;
    }

    @Override
    public HttpResponse createResponse() {
        return null;
    }
}
