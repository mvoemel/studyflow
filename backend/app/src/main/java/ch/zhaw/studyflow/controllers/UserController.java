package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.annotations.*;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.CommonClaims;
import ch.zhaw.studyflow.webserver.security.authentication.PrincipalProvider;
import ch.zhaw.studyflow.webserver.security.authentication.jwt.JwtHashAlgorithm;
import ch.zhaw.studyflow.webserver.security.authentication.impls.PrincipalImpl;
import ch.zhaw.studyflow.webserver.security.authentication.jwt.JwtPrincipalProvider;
import ch.zhaw.studyflow.webserver.security.authentication.jwt.JwtPrincipalProviderOptions;

import java.time.Duration;
import java.util.ArrayList;

@Route(path = "user")
public class UserController {
    public UserController() {

    }

    // user/current/1:2/a/b/c
    @Route(path = "current/{userId}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getUser(RequestContext context) {
        PrincipalProvider principalProvider = new JwtPrincipalProvider(
                new JwtPrincipalProviderOptions("secret", JwtHashAlgorithm.HS256, "jwt", Duration.ofDays(32)),
                new ArrayList<>()
        );
        HttpResponse response = context.getRequest().createResponse();
        PrincipalImpl principal = new PrincipalImpl();
        principal.addClaim(CommonClaims.USERID, 1243);
        principal.addClaim(CommonClaims.AUTHENTICATED, true);
        principalProvider.setPrincipal(response, principal);
        response.setStatusCode(HttpStatusCode.OK);
        return response;
    }
}
