package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;

import java.lang.reflect.Method;

public record EndpointMetadata(HttpMethod method, RestRoute route, Class<?> controller, Method endpoint) {

}
