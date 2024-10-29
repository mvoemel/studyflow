package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.webserver.http.HttpResponse;

/**
 * A request processor is a participant in the request processing pipeline.
 * It can inspect a request and manipulate it and it's response based on its content.
 */
public interface RequestProcessor {
    /**
     * Processes the passed request and then calls the next processor in the pipeline.
     * When the followed processor returns, the processor can manipulate the response
     * and pass it back to the previous processor.
     * @param context The context of the request.
     * @return The response of the request.
     */
    HttpResponse process(RequestContext context);
}
