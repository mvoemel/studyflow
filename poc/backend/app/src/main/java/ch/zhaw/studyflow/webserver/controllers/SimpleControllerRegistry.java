package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.attributes.Endpoint;
import ch.zhaw.studyflow.webserver.attributes.Route;
import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;

import java.lang.reflect.Method;
import java.util.*;


public class SimpleControllerRegistry implements ControllerRegistry {
    private List<ControllerMetadata> registeredControllers;


    public SimpleControllerRegistry() {
        this.registeredControllers  = new ArrayList<>();
    }


    @Override
    public void registerController(Class<?> controller) {
        if (registeredControllers.stream().noneMatch(metadata -> metadata.clazz().equals(controller))) {
            ControllerMetadata metadata = collectMetadata(controller);
            registeredControllers.add(metadata);
        }
    }

    @Override
    public List<ControllerMetadata> getRegisteredControllers() {
        return Collections.unmodifiableList(registeredControllers);
    }


    private ControllerMetadata collectMetadata(Class<?> controller) {
        Route controllerRoute = controller.getAnnotation(Route.class);

        List<EndpointMetadata> endpoints = new ArrayList<>();
        for (Method method : controller.getMethods()) {
            Route methodRoute = method.getAnnotation(Route.class);
            if (methodRoute != null) {
                endpoints.add(processEndpoint(method, methodRoute));
            }
        }

        return new ControllerMetadata(controller, RestRoute.of(controllerRoute.path()), endpoints);
    }

    private EndpointMetadata processEndpoint(Method method, Route routeAnnotation) {
        Endpoint httpMethod = method.getAnnotation(Endpoint.class);
        if (httpMethod == null) {
            throw new IllegalArgumentException("A Method annotated with 'Route' must have an HttpMethod annotation");
        }

        RestRoute route = RestRoute.of(routeAnnotation.path());
        return new EndpointMetadata(
                httpMethod.method(),
                route,
                method.getDeclaringClass(),
                method
        );
    }
}
