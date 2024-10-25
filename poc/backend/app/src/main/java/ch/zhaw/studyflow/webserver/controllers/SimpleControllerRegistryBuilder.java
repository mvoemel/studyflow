package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.services.ServiceCollection;
import ch.zhaw.studyflow.webserver.annotations.Endpoint;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class SimpleControllerRegistryBuilder implements ControllerRegistryBuilder {
    private static final Logger LOGGER = Logger.getLogger(SimpleControllerRegistryBuilder.class.getName());

    private final List<ControllerRegistration> registrations;


    public SimpleControllerRegistryBuilder() {
        this.registrations = new ArrayList<>();
    }


    @Override
    public <C> ControllerRegistryBuilder register(Class<C> clazz, Supplier<C> supplier) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(supplier);

        if (ensureNoExistingRegistration(clazz)) {
            LOGGER.fine(() -> "Add registration for controller '%s' with constructor (supplier).".formatted(clazz.getName()));
            registrations.add(new ControllerRegistration<>(clazz, serviceCollection -> supplier.get()));
        } else {
            LOGGER.fine(() -> "Ignoring duplicate registration for controller '%s'.".formatted(clazz.getName()));
        }
        return this;
    }

    @Override
    public <C> ControllerRegistryBuilder register(Class<C> clazz, Function<ServiceCollection, C> factory) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(factory);

        if (ensureNoExistingRegistration(clazz)) {
            LOGGER.fine(() -> "Add registration for controller '%s' with constructor (factory).".formatted(clazz.getName()));
            registrations.add(new ControllerRegistration<C>(clazz, factory));
        } else {
            LOGGER.fine(() -> "Ignoring duplicate registration for controller '%s'.".formatted(clazz.getName()));
        }
        return this;
    }

    @Override
    public ControllerRegistry build() {
        List<ControllerMetadata> collectedMetadata = new ArrayList<>();
        for (ControllerRegistration<?> registration : registrations) {
            Function<ServiceCollection, ?> factory = registration.constructor;
            collectedMetadata.add(collectMetadata(registration.controller, factory));
        }
        return new SimpleControllerRegistry(collectedMetadata);
    }

    private <C> boolean ensureNoExistingRegistration(Class<C> clazz) {
        return registrations.stream().noneMatch(registration -> registration.controller.equals(clazz));
    }

    private <T> ControllerMetadata collectMetadata(final Class<?> controller, final Function<ServiceCollection, T> factory) {
        Objects.requireNonNull(controller);

        LOGGER.fine(() -> "Collecting controller at '%s'".formatted(controller.getName()));

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
        LOGGER.fine(() -> "Collecting endpoint at '%s.%s'".formatted(handlerMethod.getDeclaringClass().getName(), handlerMethod.getName()));

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

    private record ControllerRegistration<T>(Class<T> controller, Function<ServiceCollection, T> constructor) {
    }
}
