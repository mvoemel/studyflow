package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.services.MapServiceCollectionBuilder;
import ch.zhaw.studyflow.services.ServiceCollection;
import ch.zhaw.studyflow.services.ServiceCollectionBuilder;
import ch.zhaw.studyflow.webserver.WebServer;
import ch.zhaw.studyflow.webserver.WebServerBuilder;
import ch.zhaw.studyflow.webserver.controllers.*;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import ch.zhaw.studyflow.webserver.http.pipeline.InvokeByRequestContextEndpointInvoker;
import ch.zhaw.studyflow.webserver.http.pipeline.PipelineBuilder;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class SunHttpServerWebServerBuilder implements WebServerBuilder {
    private final InetSocketAddress address;
    private final ServiceCollectionBuilder serviceCollectionBuilder;
    private final ControllerRegistryBuilder controllerRegistryBuilder;
    private Consumer<ServiceCollectionBuilder> serviceRegistrationAction;
    private Consumer<ControllerRegistryBuilder> controllerRegistrationAction;


    public SunHttpServerWebServerBuilder(InetSocketAddress address, ServiceCollectionBuilder serviceCollectionBuilder, ControllerRegistryBuilder registry) {
        this.address                    = address;
        this.serviceCollectionBuilder   = serviceCollectionBuilder;
        this.controllerRegistryBuilder  = registry;
    }


    @Override
    public WebServerBuilder configureControllers(Consumer<ControllerRegistryBuilder> configurationAction) {
        if (controllerRegistrationAction != null) {
            throw new UnsupportedOperationException("Controller can only be configured once");
        }
        controllerRegistrationAction = configurationAction;
        return this;
    }

    @Override
    public WebServerBuilder configurePipeline(Consumer<PipelineBuilder> configurationAction) {
        return null;
    }

    @Override
    public WebServerBuilder configureServices(Consumer<ServiceCollectionBuilder> serviceCollectionBuilder) {
        if (serviceRegistrationAction != null) {
            throw new UnsupportedOperationException("Services can only be configured once");
        }
        serviceRegistrationAction = serviceCollectionBuilder;
        return this;
    }

    @Override
    public WebServer build() {
        if (serviceRegistrationAction != null) {
            serviceRegistrationAction.accept(serviceCollectionBuilder);
        }
        ServiceCollection serviceCollection = serviceCollectionBuilder.build();

        if (controllerRegistrationAction == null) {
            throw new UnsupportedOperationException("Controllers must be configured");
        }

        controllerRegistrationAction.accept(controllerRegistryBuilder);
        RouteTrie routeTrie = new RouteTrie();

        ControllerRegistry controllerRegistry = controllerRegistryBuilder.build();
        controllerRegistry.getRegisteredControllers().forEach(controller -> {
            for (EndpointMetadata endpoint : controller.endpoints()) {
                routeTrie.insert(endpoint);
            }
        });

        return new SunHttpServerWebServer(address, serviceCollection, controllerRegistry, routeTrie, new InvokeByRequestContextEndpointInvoker());
    }


    public static WebServerBuilder create(InetSocketAddress address) {
        return new SunHttpServerWebServerBuilder(address, new MapServiceCollectionBuilder(), new SimpleControllerRegistryBuilder());
    }
}
