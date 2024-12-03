package ch.zhaw.studyflow.controllers;

import java.util.logging.Logger;

import ch.zhaw.studyflow.domain.studyplan.StudyplanManager;
import ch.zhaw.studyflow.webserver.annotations.Endpoint;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

public class StudyplanController {
    private static Logger LOGGER = Logger.getLogger(StudyplanController.class.getName());

    private final StudyplanManager studyplanManager;
    private final PrincipalProvider principalProvider;

    /**
     * Constructs a StudyplanController with the specified dependencies.
     *
     * @param studyplanManager the studyplan manager
     * @param authenticator the authentication handler
     */
    public StudyplanController(StudyplanManager studyplanManager, PrincipalProvider principalProvider) {
        this.studyplanManager = studyplanManager;
        this.principalProvider = principalProvider;
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
}
