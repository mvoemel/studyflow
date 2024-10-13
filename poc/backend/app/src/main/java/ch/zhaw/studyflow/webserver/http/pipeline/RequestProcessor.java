package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.webserver.http.HttpResponse;

public interface RequestProcessor {
    HttpResponse process(RequestContext context);
}
