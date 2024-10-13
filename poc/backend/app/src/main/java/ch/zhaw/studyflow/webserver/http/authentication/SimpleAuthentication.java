package ch.zhaw.studyflow.webserver.http.authentication;

import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestProcessor;

public class SimpleAuthentication implements RequestProcessor {
    private final RequestProcessor next;


    public SimpleAuthentication(RequestProcessor next) {
        this.next = next;
    }


    @Override
    public HttpResponse process(RequestContext context) {
        return next.process(context);
    }
}
