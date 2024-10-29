package ch.zhaw.studyflow.webserver.http.pipeline;

import java.util.function.Function;

public interface PipelineBuilder {
    PipelineBuilder addProcessor(Function<RequestProcessor, RequestProcessor> creator);
    void setEndpointInvoker(RequestProcessor invoker);
}
