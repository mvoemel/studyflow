package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.annotations.*;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.TextContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

import java.util.Random;

@Route(path = "user")
public class UserController {
    private final AuthenticationHandler authenticator;
    private final PrincipalProvider principalProvider;

    public UserController(AuthenticationHandler authenticator, PrincipalProvider principalProvider) {
        this.authenticator      = authenticator;
        this.principalProvider  = principalProvider;
    }

    // user/current/1:2/a/b/c
    @Route(path = "login")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse login(RequestContext context) {

        HttpResponse response = context.getRequest().createResponse();
        Principal principal = principalProvider.getPrincipal(context.getRequest());
        principal.addClaim(CommonClaims.USER_ID, new Random().nextInt());
        principal.addClaim(CommonClaims.AUTHENTICATED, true);
        principalProvider.setPrincipal(response, principal);
        response.setStatusCode(HttpStatusCode.OK);
        return response;
    }

    // user/current/1:2/a/b/c
    @Route(path = "test")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse logout(RequestContext context) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> context.getRequest()
                 .createResponse()
                 .setResponseBody(TextContent.writableOf("UserId " + principal.getClaim(CommonClaims.USER_ID).get()))
                 .setStatusCode(HttpStatusCode.OK));
    }
}
