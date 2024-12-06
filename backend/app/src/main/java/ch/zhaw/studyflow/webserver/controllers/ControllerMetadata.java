package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;

import java.util.List;

/**
 * Metadata for a controller.
 * @param clazz The class of the controller.
 * @param factory The factory to create the controller.
 * @param route The route of the controller.
 * @param endpoints The endpoints contained by the controller.
 * @param <C> The type of the controller.
 */
public record ControllerMetadata<C>(Class<C> clazz, ControllerFactory<C> factory, RestRoute route, List<EndpointMetadata> endpoints) {
}
