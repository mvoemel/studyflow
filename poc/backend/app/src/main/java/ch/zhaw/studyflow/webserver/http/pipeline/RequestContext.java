package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.http.CaptureContainer;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

public interface RequestContext {
    EndpointMetadata getTarget();

    CaptureContainer getUrlCaptures();

    HttpRequest getRequest();
    HttpResponse getResponse();

    void setResponse(HttpResponse response);
}
