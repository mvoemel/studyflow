package ch.zhaw.studyflow.controllers;

import java.util.Optional;
import java.util.logging.Logger;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
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

/**
 * Controller for the study plan API.
 * This controller provides endpoints for generating a study plan.
 */
@Route(path="api/studyplan")
public class StudyplanController {
    private static final Logger LOGGER = Logger.getLogger(StudyplanController.class.getName());

    private final StudyplanManager studyplanManager;
    private final AuthenticationHandler authenticationHandler;
    private final SemesterManager semesterManager;

    /**
     * Constructs a StudyplanController with the specified dependencies.
     *
     * @param studyplanManager the studyplan manager
     * @param authenticationHandler the authentication handler
     * @param semesterManager the semester manager
     */
    public StudyplanController(StudyplanManager studyplanManager, AuthenticationHandler authenticationHandler, SemesterManager semesterManager) {
        this.studyplanManager       = studyplanManager;
        this.authenticationHandler  = authenticationHandler;
        this.semesterManager        = semesterManager;
    }

    /**
     * Endpoint for adding a new studyplan.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse generateStudyplan(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);
            if (request.getRequestBody().isPresent()) {
                final Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
                
                final Optional<StudyplanParameters> parameters = request.getRequestBody().flatMap(body -> body.tryRead(StudyplanParameters.class));

                if (parameters.isPresent() && userId.isPresent()) {
                    Long calendarId = studyplanManager.createStudyplan(parameters.get(),  userId.get());
                    Semester semester =  semesterManager.getSemesterById(parameters.get().getSemesterId());

                    if (semester != null) {
                        semester.setCalendarId(calendarId);
                    }

                    if (calendarId != null) {
                        response.setResponseBody(JsonContent.writableOf(calendarId))
                                .setStatusCode(HttpStatusCode.CREATED);
                        LOGGER.info("Studyplan created successfully");
                    } else {
                        response.setResponseBody(JsonContent.writableOf(null))
                                .setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
                        LOGGER.warning("Studyplan creation failed");
                    }
                }
            }
                return response;
        });    
    }
}

