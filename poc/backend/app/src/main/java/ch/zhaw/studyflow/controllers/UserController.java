package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.annotations.*;
import ch.zhaw.studyflow.webserver.contents.TextContent;

@Route(path = "user")
public class UserController {


    // user/current/1:2/a/b/c
    @Route(path = "current/{a}:{b}/a/b/c")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getUser(HttpRequest request, String a, String b) {
        return request.createResponse()
                .setResponseContent(new TextContent("Hello World"))
                .setStatusCode(200);
    }

    // user/{userId}
    @Route(path = "delete/{userId}")
    @Endpoint(method = HttpMethod.GET)
    public void getUserById(HttpRequest request, int userId) {
    }
}
