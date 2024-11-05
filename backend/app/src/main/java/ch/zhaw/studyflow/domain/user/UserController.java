package ch.zhaw.studyflow.domain.user;

import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.annotations.*;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;

@Route(path = "user")
public class UserController {
    // user/current/1:2/a/b/c
    @Route(path = "current/{userId}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getUser(RequestContext context) {
        User user = new User();
        user.setName("Max Muster");
        return context.getRequest().createResponse()
                .setResponseBody(JsonContent.writableOf(user))
                .setStatusCode(HttpStatusCode.OK);

    }

    // user/{userId}
    @Route(path = "delete/{userId}")
    @Endpoint(method = HttpMethod.GET)
    public void getUserById(HttpRequest request, int userId) {
    }
}
