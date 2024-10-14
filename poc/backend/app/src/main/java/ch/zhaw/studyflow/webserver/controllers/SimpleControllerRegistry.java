package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.annotations.Endpoint;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;


public class SimpleControllerRegistry implements ControllerRegistry {
    private static final Logger logger = Logger.getLogger(SimpleControllerRegistry.class.getName());

    private final List<ControllerMetadata> registeredControllers;


    public SimpleControllerRegistry() {
        this.registeredControllers  = new ArrayList<>();
    }


    @Override
    public void registerController(Class<?> controller) {
        if (registeredControllers.stream().noneMatch(metadata -> metadata.clazz().equals(controller))) {
            registeredControllers.add(collectMetadata(controller));
        }
    }

    @Override
    public List<ControllerMetadata> getRegisteredControllers() {
        return Collections.unmodifiableList(registeredControllers);
    }


    private ControllerMetadata collectMetadata(Class<?> controller) {
        Objects.requireNonNull(controller);

        logger.fine(() -> "Collecting controller at '%s'".formatted(controller.getName()));

        Route controllerRouteAnnotation = controller.getAnnotation(Route.class);

        RestRoute controllerRoute = RestRoute.of(controllerRouteAnnotation.path());
        List<EndpointMetadata> endpoints = new ArrayList<>();
        for (Method method : controller.getMethods()) {
            Endpoint endpointAnnotation = method.getAnnotation(Endpoint.class);
            if (endpointAnnotation != null) {
                endpoints.add(processEndpoint(controllerRoute, method, endpointAnnotation));
            }
        }

        return new ControllerMetadata(controller, RestRoute.of(controllerRouteAnnotation.path()), endpoints);
    }

    private EndpointMetadata processEndpoint(RestRoute controllerRoute,
                                             Method handlerMethod,
                                             Endpoint endpointAnnotation) {
        logger.fine(() -> "Collecting endpoint at '%s.%s'".formatted(handlerMethod.getDeclaringClass().getName(), handlerMethod.getName()));

        Objects.requireNonNull(controllerRoute);
        Objects.requireNonNull(handlerMethod);
        Objects.requireNonNull(endpointAnnotation);

        Route routeAnnotation = handlerMethod.getAnnotation(Route.class);
        RestRoute route = routeAnnotation == null ? RestRoute.of("/") : RestRoute.of(routeAnnotation.path());
        return new EndpointMetadata(
                endpointAnnotation.method(),
                concatRoutes(controllerRoute, route),
                handlerMethod.getDeclaringClass(),
                handlerMethod
        );
    }

    private static RestRoute concatRoutes(RestRoute controllerRoute, RestRoute endpointRoute) {
        return new RestRoute(
                Stream.concat(
                    controllerRoute.segments().stream(),
                    endpointRoute.segments().stream()
                ).toList()
        );
    }
}
