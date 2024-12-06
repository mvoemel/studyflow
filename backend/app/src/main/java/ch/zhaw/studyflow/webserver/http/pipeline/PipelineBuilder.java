package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.webserver.http.HttpResponse;

import java.util.function.Function;

/**
 * A builder for creating a pipeline.
 */
public interface PipelineBuilder {
    /**
     * Adds a processor to the pipeline.
     * @param creator The creator function that creates the processor.
     * @return The pipeline builder.
     */
    PipelineBuilder addProcessor(Function<RequestProcessor, HttpResponse> creator);

    /**
     * Sets the invoker that is called at the end of the pipeline.
     * The endpoint invoker is responsible for invoking the endpoint at the end of the pipeline.
     * @param invoker The endpoint invoker.
     */
    void setEndpointInvoker(RequestProcessor invoker);
}
