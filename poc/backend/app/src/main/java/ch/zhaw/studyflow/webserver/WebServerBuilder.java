package ch.zhaw.studyflow.webserver;

import ch.zhaw.studyflow.webserver.controllers.ControllerRegistry;
import ch.zhaw.studyflow.webserver.http.pipeline.PipelineBuilder;

import java.util.function.Consumer;

public interface WebServerBuilder {
    WebServerBuilder configureControllers(Consumer<ControllerRegistry> configurationAction);

    WebServerBuilder configurePipeline(Consumer<PipelineBuilder> configurationAction);

    WebServer build();
}
