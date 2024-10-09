package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.controllers.routing.RouteSegment;

import java.util.List;

public record ControllerMetadata(List<RouteSegment> route, List<EndpointMetadata> endpoints) {
}
