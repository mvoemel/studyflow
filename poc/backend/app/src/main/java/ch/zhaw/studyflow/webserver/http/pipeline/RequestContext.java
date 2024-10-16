package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.http.CaptureContainer;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

import java.util.Map;

public interface RequestContext {
    EndpointMetadata getTarget();

    CaptureContainer getCaptureContainer();

    HttpRequest getRequest();
    HttpResponse getResponse();

    void setResponse(HttpResponse response);
}
