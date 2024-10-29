package ch.zhaw.studyflow.webserver;

import ch.zhaw.studyflow.webserver.contents.BodyContent;

public interface HttpResponse extends HttpRequest {
    HttpResponse setResponseContent(BodyContent content);

    HttpResponse setStatusCode(int i);

    BodyContent getResponseContent();
}
