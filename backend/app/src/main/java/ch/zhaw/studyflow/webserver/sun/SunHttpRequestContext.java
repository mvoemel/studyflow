package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.http.CaptureContainer;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;

/**
 * The SunHttpRequestContext class is an implementation of the RequestContext interface provided by the Sun HTTP server.
 * It represents the context of an HTTP request.
 */
public class SunHttpRequestContext implements RequestContext {
    private final EndpointMetadata target;
    private final HttpRequest request;
    private final CaptureContainer captureContainer;
    private HttpResponse response;

    public SunHttpRequestContext(EndpointMetadata metadata, HttpRequest request, CaptureContainer captureContainer) {
        this.target             = metadata;
        this.request            = request;
        this.captureContainer   = captureContainer;
    }

    @Override
    public EndpointMetadata getTarget() {
        return target;
    }

    @Override
    public CaptureContainer getUrlCaptures() {
        return captureContainer;
    }

    @Override
    public HttpRequest getRequest() {
        return request;
    }
}
