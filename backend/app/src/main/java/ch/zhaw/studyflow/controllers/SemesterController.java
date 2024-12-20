package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.SemesterDeo;
import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.utils.LongUtils;
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
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing semesters.
 */
@Route(path = "api/semesters")
public class SemesterController {

    private final AuthenticationHandler authenticationHandler;
    private final SemesterManager semesterManager;

    public SemesterController(AuthenticationHandler authenticationHandler, SemesterManager semesterManager) {
        this.authenticationHandler  = authenticationHandler;
        this.semesterManager        = semesterManager;
    }

    @Route(path = "")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse createSemester(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = context.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);
            if (request.getRequestBody().isPresent()) {
                Optional<SemesterDeo> semesterDeo = request.getRequestBody()
                        .flatMap(body -> body.tryRead(SemesterDeo.class));
                Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
                if (semesterDeo.isPresent() && semesterDeo.get().isValid()) {
                    semesterDeo.map(deo -> {
                        Semester semester = new Semester();
                        semester.setName(deo.getName());
                        semester.setDescription(deo.getDescription());
                        semester.setDegreeId(deo.getDegreeId());
                        semester.setUserId(userId.get());
                        return semester;
                    }).ifPresentOrElse(semester -> {
                        semesterManager.createSemester(semester, semester.getDegreeId(), semester.getUserId());
                        response.setResponseBody(JsonContent.writableOf(semester))
                                .setStatusCode(HttpStatusCode.CREATED);
                    }, () -> {
                        response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                    });
                }
            }
            return response;
        });
    }

    @Route(path = "")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getSemesters(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = context.getRequest().createResponse();
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            if (userId.isPresent()) {
                List<Semester> semester = semesterManager.getSemestersForStudent(userId.get());
                response.setResponseBody(JsonContent.writableOf(semester))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    @Route(path = "{id}")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse updateSemester(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = context.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);
            context.getUrlCaptures().get("id").flatMap(LongUtils::tryParseLong).ifPresent(semesterId -> {
                request.getRequestBody().flatMap(body -> body.tryRead(SemesterDeo.class))
                        .ifPresent(semesterDeo -> {
                            if (semesterDeo.isValid()) {
                                final Semester semester = semesterManager.getSemesterById(semesterId);
                                if (semester != null) {
                                    final Semester updatedSemester = new Semester();
                                    updatedSemester.setName(semesterDeo.getName());
                                    updatedSemester.setDescription(semesterDeo.getDescription());
                                    semesterManager.updateSemester(updatedSemester);
                                    response.setStatusCode(HttpStatusCode.OK);
                                } else {
                                    response.setStatusCode(HttpStatusCode.NOT_FOUND);
                                }
                            }
                        });
            });
            return response;
        });
    }

    @Route(path = "{id}")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteSemester(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = context.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);
            principal.getClaim(CommonClaims.USER_ID).ifPresent(userId ->
                    context.getUrlCaptures().get("id")
                            .flatMap(LongUtils::tryParseLong)
                            .ifPresent(semesterId -> {
                                        Semester semester = semesterManager.getSemesterById(semesterId);
                                        if (semester != null) {
                                            if (semester.getUserId() == userId) {
                                                semesterManager.deleteSemester(semesterId);
                                                response.setStatusCode(HttpStatusCode.OK);
                                            } else {
                                                response.setStatusCode(HttpStatusCode.FORBIDDEN);
                                            }
                                        } else {
                                            response.setStatusCode(HttpStatusCode.NOT_FOUND);
                                        }
                                    }
                            )
            );
            return response;
        });
    }


}
