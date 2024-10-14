package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.attributes.HttpMethod;
import ch.zhaw.studyflow.webserver.attributes.Route;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteSegment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EndpointCollector {
    public static ControllerMetadata collect(Class<?> controller) {
        Route controllerRoute = controller.getAnnotation(Route.class);

        List<EndpointMetadata> endpoints = new ArrayList<>();
        for (Method method : controller.getMethods()) {
            Route methodRoute = method.getAnnotation(Route.class);
            if (methodRoute != null) {
                endpoints.add(processEndpoint(method, methodRoute));
            }
        }
        return new ControllerMetadata(RouteSegment.segmentize(controllerRoute.path()), endpoints);
    }

    private static EndpointMetadata processEndpoint(Method method, Route routeAnnotation) {
        HttpMethod httpMethod = method.getAnnotation(HttpMethod.class);
        if (httpMethod == null) {
            throw new IllegalArgumentException("A Method annotated with 'Route' must have an HttpMethod annotation");
        }

        return new EndpointMetadata(
                httpMethod.method(),
                RouteSegment.segmentize(routeAnnotation.path()),
                method.getDeclaringClass(),
                method
        );
    }
}
