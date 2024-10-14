package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.HttpMethod;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteSegment;

import java.lang.reflect.Method;
import java.util.List;

public record EndpointMetadata(HttpMethod method, List<RouteSegment> segments, Class<?> controller, Method endpoint) {

}
