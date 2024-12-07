package ch.zhaw.studyflow.controllers;

import java.util.Optional;
import java.util.logging.Logger;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.domain.studyplan.StudyplanManager;
import ch.zhaw.studyflow.webserver.annotations.Endpoint;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;

public class StudyplanController {
    private static Logger LOGGER = Logger.getLogger(StudyplanController.class.getName());

    private final StudyplanManager studyplanManager;
    private final AuthenticationHandler authenticationHandler;

    /**
     * Constructs a StudyplanController with the specified dependencies.
     *
     * @param studyplanManager the studyplan manager
     * @param authenticator the authentication handler
     */
    public StudyplanController(StudyplanManager studyplanManager, AuthenticationHandler authenticationHandler) {
        this.studyplanManager       = studyplanManager;
        this.authenticationHandler  = authenticationHandler;
    }

    /**
     * Endpoint for adding a new studyplan.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse addStudyplan(RequestContext context) {
        final HttpRequest request = context.getRequest();
        //TODO: Implement the addStudyplan method
        return null;
    }

    public HttpResponse generateStudyplan(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticationHandler.handleIfAuthenticated(request, principal -> {
                final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

                final Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
                
                final Optional<StudyplanParameters> parameters = request.getRequestBody().flatMap(body -> body.tryRead(StudyplanParameters.class));

                if (parameters.isPresent() && userId.isPresent()) {
                    //studyplanManager.createStudyplan(...);
                    response.setResponseBody(JsonContent.writableOf(null))
                            .setStatusCode(HttpStatusCode.OK);
                }

                return response;
        });
    }
}
