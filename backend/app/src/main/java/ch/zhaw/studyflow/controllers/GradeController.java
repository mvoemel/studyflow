package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.services.persistence.GradeDao;
import ch.zhaw.studyflow.webserver.annotations.Endpoint;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Controller for handling grade-related HTTP requests.
 * Provides endpoints for creating, reading, updating, and deleting grades.
 */
@Route(path = "api/grades")
public class GradeController {
    private static final Logger logger = Logger.getLogger(GradeController.class.getName());
    private final GradeDao gradeDao;
    private final AuthenticationHandler authenticator;

    /**
     * Constructs a GradeController with the specified grade DAO and authenticator.
     *
     * @param gradeDao the DAO for grade operations
     * @param authenticator the handler for authentication
     */
    public GradeController(GradeDao gradeDao, AuthenticationHandler authenticator) {
        this.gradeDao = gradeDao;
        this.authenticator = authenticator;
    }

    /**
     * Endpoint for creating a new grade.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse createGrade(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Grade.class))
                    .flatMap(grade -> {
                        gradeDao.create(grade);
                        return Optional.of(grade);
                    })
                    .ifPresentOrElse(
                            grade -> {
                                response.setResponseBody(JsonContent.writableOf(grade))
                                        .setStatusCode(HttpStatusCode.CREATED);
                            },
                            () -> {
                                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                            }
                    );
            return response;
        });
    }

    /**
     * Endpoint for updating an existing grade.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{gradeId}")
    @Endpoint(method = HttpMethod.PUT)
    public HttpResponse updateGrade(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<String> gradeIdOpt = context.getUrlCaptures().get("gradeId");
            if (gradeIdOpt.isPresent()) {
                long gradeId = Long.parseLong(gradeIdOpt.get());
                request.getRequestBody()
                        .flatMap(body -> body.tryRead(Grade.class))
                        .ifPresentOrElse(
                                grade -> {
                                    grade.setId(gradeId);
                                    gradeDao.update(grade);
                                    response.setResponseBody(JsonContent.writableOf(grade))
                                            .setStatusCode(HttpStatusCode.OK);
                                },
                                () -> {
                                    response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                                }
                        );
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    /**
     * Endpoint for deleting an existing grade.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{gradeId}")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteGrade(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<String> gradeIdOpt = context.getUrlCaptures().get("gradeId");
            if (gradeIdOpt.isPresent()) {
                long gradeId = Long.parseLong(gradeIdOpt.get());
                gradeDao.delete(gradeId);
                response.setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    /**
     * Endpoint for retrieving grades by module ID.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "modules/{modId}/grades")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getGradesByModId(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<String> modIdOpt = context.getUrlCaptures().get("modId");
            if (modIdOpt.isPresent()) {
                long modId = Long.parseLong(modIdOpt.get());
                List<Grade> grades = gradeDao.readByModule(modId);
                response.setResponseBody(JsonContent.writableOf(grades))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }
}