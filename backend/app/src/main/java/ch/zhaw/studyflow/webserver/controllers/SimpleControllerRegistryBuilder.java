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

/**
 * A builder for creating a controller registry.
 */
public class SimpleControllerRegistryBuilder implements ControllerRegistryBuilder {
    private static final Logger LOGGER = Logger.getLogger(SimpleControllerRegistryBuilder.class.getName());

    private final List<ControllerRegistration<?>> registrations;


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
            registrations.add(new ControllerRegistration<>(clazz, factory));
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

    /**
     * Ensures that there is no existing registration for a controller.
     * @param clazz The class of the controller.
     * @param <C> The type of the controller.
     * @return {@code true} if there is no existing registration, {@code false} otherwise.
     */
    private <C> boolean ensureNoExistingRegistration(Class<C> clazz) {
        return registrations.stream().noneMatch(registration -> registration.controller.equals(clazz));
    }

    /**
     * Collects the metadata of a controller.
     * @param registration The registration of the controller.
     * @param <C> The type of the controller.
     * @return The metadata of the controller.
     */
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

    /**
     * Processes an endpoint method and creates the metadata for it.
     * @param controllerMetadata The metadata of the controller.
     * @param controllerRoute The route of the controller.
     * @param handlerMethod The method of the endpoint.
     * @param endpointAnnotation The annotation of the endpoint.
     * @param <C> The type of the controller.
     * @return The metadata of the endpoint.
     */
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

    /**
     * Concatenates the route of a controller with the route of an endpoint.
     * @param controllerRoute The route of the controller.
     * @param endpointRoute The route of the endpoint.
     * @return The concatenated route.
     */
    private static RestRoute concatRoutes(RestRoute controllerRoute, RestRoute endpointRoute) {
        return new RestRoute(
                Stream.concat(
                        controllerRoute.segments().stream(),
                        endpointRoute.segments().stream()
                ).toList()
        );
    }

    /**
     * A record for holding the information about a registered controller until the registry is built.
     * @param controller The controller class.
     * @param factory The factory for creating the controller.
     * @param <C> The type of the controller.
     */
    private record ControllerRegistration<C>(Class<C> controller, ControllerFactory<C> factory) {
    }
}
