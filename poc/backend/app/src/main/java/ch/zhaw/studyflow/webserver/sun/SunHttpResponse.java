package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.BodyContent;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SunHttpResponse implements HttpResponse {
    private final SunHttpRequest request;
    private BodyContent responseContent;
    private HttpStatusCode statusCode;

    public SunHttpResponse(SunHttpRequest request) {
        this.request = request;
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
    public BodyContent getResponseContent() {
        return responseContent;
    }
}
