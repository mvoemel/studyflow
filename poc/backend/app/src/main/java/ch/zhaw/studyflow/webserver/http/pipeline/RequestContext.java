package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.http.CaptureContainer;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

/**
 * The {@code RequestContext} contains all information about the request and its response.
 * It is passed through the request processing pipeline and can be used to manipulate the request and response.
 */
public interface RequestContext {
    /**
     * Gets the metadata object which contains information about the targeted endpoint.
     * @return The target endpoint metadata.
     */
    EndpointMetadata getTarget();

    /**
     * Gets the capture container which contains all URL captures.
     * @return The capture container.
     */
    CaptureContainer getUrlCaptures();

    /**
     * Gets the request object.
     * @return The request object.
     */
    HttpRequest getRequest();

    /**
     * Gets the response object.
     * @return The response object.
     */
    HttpResponse getResponse();

    /**
     * Sets the response object.
     * @param response The response object.
     */
    void setResponse(HttpResponse response);
}
