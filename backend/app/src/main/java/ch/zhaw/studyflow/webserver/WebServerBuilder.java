package ch.zhaw.studyflow.webserver;

import ch.zhaw.studyflow.services.ServiceCollectionBuilder;
import ch.zhaw.studyflow.webserver.controllers.ControllerRegistryBuilder;
import ch.zhaw.studyflow.webserver.http.pipeline.PipelineBuilder;

import java.util.function.Consumer;

/**
 * Builder class for the WebServer.
 * This class provides a fluent builder API for configuring the WebServer.
 */
public interface WebServerBuilder {
    /**
     * Configures the controllers for the WebServer.
     * This method is used to register and configure controllers for the WebServer.
     * @param configurationAction The configuration action to apply to the ControllerRegistryBuilder
     * @return The WebServerBuilder instance
     */
    WebServerBuilder configureControllers(Consumer<ControllerRegistryBuilder> configurationAction);

    /**
     * Configures the pipeline for the WebServer.
     * This method is used to configure the pipeline for the WebServer.
     * @param configurationAction The configuration action to apply to the PipelineBuilder
     * @return The WebServerBuilder instance
     */
    WebServerBuilder configurePipeline(Consumer<PipelineBuilder> configurationAction);

    /**
     * Configures the available services for the WebServer.
     * Registered services during e.g. the construction of controllers or other services.
     * @param serviceCollectionBuilder The service collection builder
     * @return The WebServerBuilder instance
     */
    WebServerBuilder configureServices(Consumer<ServiceCollectionBuilder> serviceCollectionBuilder);

    /**
     * Builds the WebServer instance.
     * @return The WebServer instance
     */
    WebServer build();
}
