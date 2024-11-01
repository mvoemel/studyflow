package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;

import java.util.List;

public record ControllerMetadata<C>(Class<C> clazz, ControllerFactory<C> factory, RestRoute route, List<EndpointMetadata> endpoints) {
}
