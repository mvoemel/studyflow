package ch.zhaw.studyflow.webserver.http;

import ch.zhaw.studyflow.webserver.http.contents.BodyContent;

public interface HttpResponse extends HttpRequest {
    HttpResponse setResponseContent(BodyContent content);

    HttpResponse setStatusCode(HttpStatusCode statusCode);

    BodyContent getResponseContent();
}
