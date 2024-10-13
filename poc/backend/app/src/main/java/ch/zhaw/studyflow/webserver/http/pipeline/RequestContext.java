package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

public interface RequestContext {
    EndpointMetadata getTarget();

    HttpRequest getRequest();

    HttpResponse getResponse();
    void setResponse(HttpResponse response);
}
