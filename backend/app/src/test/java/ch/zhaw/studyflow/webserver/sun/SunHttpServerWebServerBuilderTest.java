package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.services.ServiceCollection;
import ch.zhaw.studyflow.services.ServiceCollectionBuilder;
import ch.zhaw.studyflow.webserver.WebServer;
import ch.zhaw.studyflow.webserver.controllers.ControllerMetadata;
import ch.zhaw.studyflow.webserver.controllers.ControllerRegistry;
import ch.zhaw.studyflow.webserver.controllers.ControllerRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SunHttpServerWebServerBuilderTest {

    private InetSocketAddress address;
    private ServiceCollectionBuilder serviceCollectionBuilder;
    private ControllerRegistryBuilder controllerRegistryBuilder;
    private SunHttpServerWebServerBuilder builder;

    @BeforeEach
    void setUp() {
        address = new InetSocketAddress(8080);
        serviceCollectionBuilder = mock(ServiceCollectionBuilder.class);
        controllerRegistryBuilder = mock(ControllerRegistryBuilder.class);
        builder = new SunHttpServerWebServerBuilder(address, serviceCollectionBuilder, controllerRegistryBuilder);
    }

    @Test
    void testConfigureControllers() {
        Consumer<ControllerRegistryBuilder> controllerConfig = mock(Consumer.class);
        builder.configureControllers(controllerConfig);
        assertThrows(UnsupportedOperationException.class, () -> builder.configureControllers(controllerConfig));
    }

    @Test
    void testConfigureServices() {
        Consumer<ServiceCollectionBuilder> serviceConfig = mock(Consumer.class);
        builder.configureServices(serviceConfig);
        assertThrows(UnsupportedOperationException.class, () -> builder.configureServices(serviceConfig));
    }

    @Test
    void testBuild() {
        Consumer<ControllerRegistryBuilder> controllerConfig = mock(Consumer.class);
        Consumer<ServiceCollectionBuilder> serviceConfig = mock(Consumer.class);
        ServiceCollection serviceCollection = mock(ServiceCollection.class);
        ControllerRegistry controllerRegistry = mock(ControllerRegistry.class);
        ControllerMetadata<?> controllerMetadata = mock(ControllerMetadata.class);

        when(serviceCollectionBuilder.build()).thenReturn(serviceCollection);
        when(controllerRegistryBuilder.build()).thenReturn(controllerRegistry);
        when(controllerRegistry.getRegisteredControllers()).thenReturn(List.of(controllerMetadata));

        builder.configureControllers(controllerConfig);
        builder.configureServices(serviceConfig);

        WebServer webServer = builder.build();

        assertNotNull(webServer);
        assertTrue(webServer instanceof SunHttpServerWebServer);
    }

    @Test
    void testBuildWithoutControllerConfig() {
        assertThrows(UnsupportedOperationException.class, () -> builder.build());
    }
}