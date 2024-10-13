package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

public class HttpRequestContext implements RequestContext {
    private final EndpointMetadata target;
    private final HttpRequest request;
    private HttpResponse response;

    public HttpRequestContext(EndpointMetadata metadata, HttpRequest request) {
        this.target     = metadata;
        this.request    = request;
    }

    @Override
    public EndpointMetadata getTarget() {
        return target;
    }

    @Override
    public HttpRequest getRequest() {
        return request;
    }

    @Override
    public HttpResponse getResponse() {
        return response;
    }

    @Override
    public void setResponse(HttpResponse response) {
        if (this.response != null) {
            throw new IllegalStateException("Response already set");
        }
        this.response = response;
    }
}
