package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;

import java.lang.reflect.Method;

/**
 * The {@code EndpointMetadata} class represents metadata about an endpoint.
 * @param controller The controller class containing the endpoint.
 * @param method The HTTP method used to access the endpoint.
 * @param route The route metadata of the endpoint.
 * @param endpoint The endpoint method.
 */
public record EndpointMetadata(ControllerMetadata<?> controller, HttpMethod method, RestRoute route, Method endpoint) {
}
