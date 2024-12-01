package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.ModuleDeo;
import ch.zhaw.studyflow.controllers.deo.ModuleGrade;
import ch.zhaw.studyflow.controllers.deo.SemesterDeo;
import ch.zhaw.studyflow.controllers.deo.SemesterGrade;
import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.services.persistence.GradeDao;
import ch.zhaw.studyflow.services.persistence.ModuleDao;
import ch.zhaw.studyflow.services.persistence.SemesterDao;
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
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller for handling grade-related requests.
 */
@Route(path = "api/degrees")
public class GradeController {
    private static final Logger logger = Logger.getLogger(GradeController.class.getName());
    private final GradeDao gradeDao;
    private final SemesterDao semesterDao;
    private final ModuleDao moduleDao;
    private final AuthenticationHandler authenticator;

    /**
     * Constructs a GradeController with the specified GradeDao and AuthenticationHandler.
     *
     * @param gradeDao the GradeDao to use for data access.
     * @param authenticator the AuthenticationHandler to use for authentication.
     */
    public GradeController(GradeDao gradeDao,SemesterDao semesterDao, ModuleDao moduleDao, AuthenticationHandler authenticator) {
        this.gradeDao = gradeDao;
        this.semesterDao = semesterDao;
        this.moduleDao = moduleDao;
        this.authenticator = authenticator;
    }

    /**
     * Handles GET requests to retrieve grades by degree ID.
     *
     * @param context the request context.
     * @return the HTTP response.
     */
    @Route(path = "degrees/{degreeId}/grades")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getGradesByDegreeId(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<String> degreeIdOpt = context.getUrlCaptures().get("degreeId");
            if (degreeIdOpt.isPresent()) {
                long degreeId = Long.parseLong(degreeIdOpt.get());
                List<Semester> semesters = semesterDao.readByDegreeId(degreeId);
                List<SemesterGrade> semesterGrades = semesters.stream().map(semester -> {
                    List<Module> modules = moduleDao.readBySemesterId(semester.getId());
                    List<ModuleGrade> moduleGrades = modules.stream().map(module -> {
                        List<Grade> grades = gradeDao.readByModule(module.getId());
                        return new ModuleGrade(module.getId(), module.getName(), grades);
                    }).collect(Collectors.toList());
                    return new SemesterGrade(semester.getId(), semester.getName(), moduleGrades);
                }).collect(Collectors.toList());

                response.setResponseBody(JsonContent.writableOf(semesterGrades))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    /**
     * Handles PATCH requests to update grades by degree ID.
     *
     * @param context the request context.
     * @return the HTTP response.
     */
    @Route(path = "degrees/{degreeId}/grades")
    @Endpoint(method = HttpMethod.PATCH)
    public HttpResponse patchGradesByDegreeId(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<String> degreeIdOpt = context.getUrlCaptures().get("degreeId");
            if (degreeIdOpt.isPresent()) {
                long degreeId = Long.parseLong(degreeIdOpt.get());
                request.getRequestBody()
                        .flatMap(body -> body.tryRead(ModuleGrade.class))
                        .ifPresentOrElse(
                                moduleGrade -> {
                                    List<Grade> grades = moduleGrade.getGrades();
                                    if (validateGrades(grades)) {
                                        gradeDao.updateByDegree(degreeId, grades);
                                        response.setStatusCode(HttpStatusCode.OK);
                                    } else {
                                        response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                                    }
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
     * Handles GET requests to retrieve the average grade value by degree ID.
     *
     * @param context the request context.
     * @return the HTTP response.
     */
    @Route(path = "degrees/{degreeId}/grades/average")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getGradesAveragesByDegreeId(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<String> degreeIdOpt = context.getUrlCaptures().get("degreeId");
            if (degreeIdOpt.isPresent()) {
                long degreeId = Long.parseLong(degreeIdOpt.get());
                List<Grade> grades = gradeDao.readByDegree(degreeId);
                double average = grades.stream().mapToDouble(Grade::getValue).average().orElse(0.0);
                response.setResponseBody(JsonContent.writableOf(Map.of("average", average)))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    /**
     * Validates the list of grades to ensure the total percentage is 1.0.
     *
     * @param grades the list of grades to validate.
     * @return true if the total percentage is 1.0, false otherwise.
     */
    private boolean validateGrades(List<Grade> grades) {
        double totalPercentage = grades.stream().mapToDouble(Grade::getPercentage).sum();
        return totalPercentage == 1.0;
    }
}