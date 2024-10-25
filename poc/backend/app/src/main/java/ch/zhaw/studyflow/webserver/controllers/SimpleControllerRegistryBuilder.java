package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.annotations.Endpoint;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
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
            LOGGER.fine(() -> "Add registration for controller '%s' with factory (supplier).".formatted(clazz.getName()));
            registrations.add(new ControllerRegistration<>(clazz, serviceCollection -> supplier.get()));
        } else {
            LOGGER.fine(() -> "Ignoring duplicate registration for controller '%s'.".formatted(clazz.getName()));
        }
        return this;
    }

    @Override
    public <C> ControllerRegistryBuilder register(Class<C> clazz, ControllerFactory<C> factory) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(factory);

        if (ensureNoExistingRegistration(clazz)) {
            LOGGER.fine(() -> "Add registration for controller '%s' with factory (factory).".formatted(clazz.getName()));
            registrations.add(new ControllerRegistration<C>(clazz, factory));
        } else {
            LOGGER.fine(() -> "Ignoring duplicate registration for controller '%s'.".formatted(clazz.getName()));
        }
        return this;
    }

    @Override
    public ControllerRegistry build() {
        List<ControllerMetadata<?>> collectedMetadata = new ArrayList<>();
        for (ControllerRegistration<?> registration : registrations) {
            collectedMetadata.add(collectMetadata(registration));
        }
        return new SimpleControllerRegistry(collectedMetadata);
    }

    private <C> boolean ensureNoExistingRegistration(Class<C> clazz) {
        return registrations.stream().noneMatch(registration -> registration.controller.equals(clazz));
    }

    private <C> ControllerMetadata<C> collectMetadata(ControllerRegistration<C> registration) {
        Objects.requireNonNull(registration);

        Class<C> controllerClazz = registration.controller();
        LOGGER.finer(() -> "Collecting controller at '%s'".formatted(controllerClazz.getName()));

        final Route controllerRouteAnnotation = controllerClazz.getAnnotation(Route.class);

        final RestRoute controllerRoute = RestRoute.of(controllerRouteAnnotation.path());
        List<EndpointMetadata> endpoints = new ArrayList<>();
        ControllerMetadata<C> controllerMetadata = new ControllerMetadata<>(
                controllerClazz,
                registration.factory(),
                RestRoute.of(controllerRouteAnnotation.path()),
                Collections.unmodifiableList(endpoints)
        );
        for (Method method : controllerClazz.getMethods()) {
            final Endpoint endpointAnnotation = method.getAnnotation(Endpoint.class);
            if (endpointAnnotation != null) {
                endpoints.add(processEndpoint(controllerMetadata, controllerRoute, method, endpointAnnotation));
            }
        }

        return controllerMetadata;
    }

    private <C> EndpointMetadata processEndpoint(ControllerMetadata<C> controllerMetadata,
                                                 RestRoute controllerRoute,
                                                 Method handlerMethod,
                                                 Endpoint endpointAnnotation) {
        LOGGER.finer(() -> "Collecting endpoint at '%s.%s'".formatted(handlerMethod.getDeclaringClass().getName(), handlerMethod.getName()));

        Objects.requireNonNull(controllerRoute);
        Objects.requireNonNull(handlerMethod);
        Objects.requireNonNull(endpointAnnotation);

        Route routeAnnotation = handlerMethod.getAnnotation(Route.class);
        RestRoute route = routeAnnotation == null ? RestRoute.of("/") : RestRoute.of(routeAnnotation.path());
        return new EndpointMetadata(
                controllerMetadata,
                endpointAnnotation.method(),
                concatRoutes(controllerRoute, route),
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

    private record ControllerRegistration<C>(Class<C> controller, ControllerFactory<C> factory) {
    }
}
