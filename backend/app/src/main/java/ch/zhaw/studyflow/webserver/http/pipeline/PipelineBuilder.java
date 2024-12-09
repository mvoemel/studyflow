package ch.zhaw.studyflow.webserver.http.pipeline;

import java.util.function.Function;

/**
 * A builder interface for the servers pipeline.
 */
public interface PipelineBuilder {
    /**
     * Adds a processor to the pipeline.
     * @param creator The creator function to create the processor.
     * @return The builder instance.
     */
    PipelineBuilder addProcessor(Function<RequestProcessor, RequestProcessor> creator);

    /**
     * Sets the endpoint invoker of the pipeline.
     * @param invoker The invoker to set.
     */
    void setEndpointInvoker(RequestProcessor invoker);
}
