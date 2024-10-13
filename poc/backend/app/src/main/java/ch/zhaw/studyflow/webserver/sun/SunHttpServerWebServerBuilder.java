package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.WebServer;
import ch.zhaw.studyflow.webserver.WebServerBuilder;
import ch.zhaw.studyflow.webserver.controllers.ControllerRegistry;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.controllers.SimpleControllerRegistry;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import ch.zhaw.studyflow.webserver.http.pipeline.DirectEndpointInvoker;
import ch.zhaw.studyflow.webserver.http.pipeline.PipelineBuilder;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class SunHttpServerWebServerBuilder implements WebServerBuilder {
    private final InetSocketAddress address;
    private ControllerRegistry controllerRegistry;
    private Consumer<ControllerRegistry> controllerConfigurationAction;

    public SunHttpServerWebServerBuilder(InetSocketAddress address, ControllerRegistry registry) {
        this.address            = address;
        this.controllerRegistry = registry;
    }


    @Override
    public WebServerBuilder configureControllers(Consumer<ControllerRegistry> configurationAction) {
        if (controllerConfigurationAction != null) {
            throw new UnsupportedOperationException("Controller can only be configured once");
        }
        controllerConfigurationAction = configurationAction;
        return this;
    }

    @Override
    public WebServerBuilder configurePipeline(Consumer<PipelineBuilder> configurationAction) {
        return null;
    }

    @Override
    public WebServer build() {
        if (controllerConfigurationAction == null) {
            throw new UnsupportedOperationException("Controllers must be configured");
        }

        controllerConfigurationAction.accept(controllerRegistry);
        RouteTrie routeTrie = new RouteTrie();
        controllerRegistry.getRegisteredControllers().forEach(controller -> {
            for (EndpointMetadata endpoint : controller.endpoints()) {
                routeTrie.insert(endpoint);
            }
        });

        return new SunHttpServerWebServer(address, routeTrie, new DirectEndpointInvoker());
    }


    public static WebServerBuilder create(InetSocketAddress address) {
        return new SunHttpServerWebServerBuilder(address, new SimpleControllerRegistry());
    }
}
