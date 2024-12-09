package ch.zhaw.studyflow.webserver;

import ch.zhaw.studyflow.services.ServiceCollectionBuilder;
import ch.zhaw.studyflow.webserver.controllers.ControllerRegistryBuilder;
import ch.zhaw.studyflow.webserver.http.pipeline.PipelineBuilder;

import java.util.function.Consumer;

/**
 * A builder for a web server.
 */
public interface WebServerBuilder {
    /**
     * Configures the controllers of the web server.
     * @param configurationAction The configuration action to configure the controllers.
     * @return The builder instance.
     */
    WebServerBuilder configureControllers(Consumer<ControllerRegistryBuilder> configurationAction);

    /**
     * Configures the pipeline of the web server.
     * @param configurationAction The configuration action to configure the pipeline.
     * @return The builder instance.
     */
    WebServerBuilder configurePipeline(Consumer<PipelineBuilder> configurationAction);

    /**
     * Configures the services of the web server.
     * @param serviceCollectionBuilder The configuration action to configure the services.
     * @return The builder instance.
     */
    WebServerBuilder configureServices(Consumer<ServiceCollectionBuilder> serviceCollectionBuilder);

    /**
     * Builds the web server.
     * @return The built web server.
     */
    WebServer build();
}
