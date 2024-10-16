package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.annotations.*;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.TextContent;
import ch.zhaw.studyflow.webserver.http.cookies.Cookie;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;

@Route(path = "user")
public class UserController {
    // user/current/1:2/a/b/c
    @Route(path = "current/{userId}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getUser(RequestContext context) {

        return context.getRequest().createResponse()
                .setResponseContent(new TextContent("Hallo " + context.getCaptureContainer().get("userId").get()))
                .setStatusCode(HttpStatusCode.OK);

    }

    // user/{userId}
    @Route(path = "delete/{userId}")
    @Endpoint(method = HttpMethod.GET)
    public void getUserById(HttpRequest request, int userId) {
    }
}
