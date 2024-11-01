package ch.zhaw.studyflow.webserver;

import ch.zhaw.studyflow.services.ServiceCollectionBuilder;
import ch.zhaw.studyflow.webserver.controllers.ControllerRegistryBuilder;
import ch.zhaw.studyflow.webserver.http.pipeline.PipelineBuilder;

import java.util.function.Consumer;

public interface WebServerBuilder {
    WebServerBuilder configureControllers(Consumer<ControllerRegistryBuilder> configurationAction);

    WebServerBuilder configurePipeline(Consumer<PipelineBuilder> configurationAction);

    WebServerBuilder configureServices(Consumer<ServiceCollectionBuilder> serviceCollectionBuilder);

    WebServer build();
}
