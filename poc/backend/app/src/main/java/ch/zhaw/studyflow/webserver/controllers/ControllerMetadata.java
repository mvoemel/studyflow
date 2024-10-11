package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;

import java.util.List;

public record ControllerMetadata(Class<?> clazz, RestRoute route, List<EndpointMetadata> endpoints) {
}
