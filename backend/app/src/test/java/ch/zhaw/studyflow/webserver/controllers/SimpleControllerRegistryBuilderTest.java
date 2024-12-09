package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.webserver.annotations.Endpoint;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;
import ch.zhaw.studyflow.webserver.http.HttpMethod;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleControllerRegistryBuilderTest {

    @Test
    void testRegisterAndBuild() {
        SimpleControllerRegistryBuilder builder = new SimpleControllerRegistryBuilder();
        builder.register(TestController.class, TestController::new);

        ControllerRegistry registry = builder.build();
        List<ControllerMetadata<?>> controllers = registry.getRegisteredControllers();

        assertEquals(1, controllers.size());
        ControllerMetadata<?> metadata = controllers.get(0);
        assertEquals(TestController.class, metadata.clazz());
        assertEquals(1, metadata.endpoints().size());

        EndpointMetadata endpointMetadata = metadata.endpoints().get(0);
        assertEquals(HttpMethod.GET, endpointMetadata.method());
        assertEquals(RestRoute.of("/test"), endpointMetadata.route());
    }

    @Test
    void testDuplicateRegistration() {
        SimpleControllerRegistryBuilder builder = new SimpleControllerRegistryBuilder();
        builder.register(TestController.class, TestController::new);
        builder.register(TestController.class, TestController::new);

        ControllerRegistry registry = builder.build();
        List<ControllerMetadata<?>> controllers = registry.getRegisteredControllers();

        assertEquals(1, controllers.size());
    }

    @Test
    void testNoRegistration() {
        SimpleControllerRegistryBuilder builder = new SimpleControllerRegistryBuilder();

        ControllerRegistry registry = builder.build();
        List<ControllerMetadata<?>> controllers = registry.getRegisteredControllers();

        assertEquals(0, controllers.size());
    }

    @Test
    void testMultipleRegistrations() {
        SimpleControllerRegistryBuilder builder = new SimpleControllerRegistryBuilder();
        builder.register(TestController.class, TestController::new);
        builder.register(AnotherTestController.class, AnotherTestController::new);

        ControllerRegistry registry = builder.build();
        List<ControllerMetadata<?>> controllers = registry.getRegisteredControllers();

        assertEquals(2, controllers.size());
    }

    @Route(path = "/")
    static class TestController {
        @Endpoint(method = HttpMethod.GET)
        @Route(path = "/test")
        public void testEndpoint() {
        }
    }

    @Route(path = "/another")
    static class AnotherTestController {
        @Endpoint(method = HttpMethod.POST)
        @Route(path = "/test")
        public void anotherTestEndpoint() {
        }
    }
}