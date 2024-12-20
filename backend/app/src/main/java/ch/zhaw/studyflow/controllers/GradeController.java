package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.ModuleGrade;
import ch.zhaw.studyflow.controllers.deo.SemesterGrade;
import ch.zhaw.studyflow.domain.curriculum.*;
import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.domain.grade.GradeManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Controller for handling grade-related requests.
 */
@Route(path = "api/degrees")
public class GradeController {
    private static final Logger LOGGER = Logger.getLogger(GradeController.class.getName());
    private final SemesterManager semesterManager;
    private final ModuleManager moduleManager;
    private final GradeManager gradeManager;
    private final AuthenticationHandler authenticator;

    /**
     * Constructs a GradeController with the specified GradeDao and AuthenticationHandler.
     *
     * @param semesterManager the DegreeManager to use for degree-related operations.
     * @param moduleManager   the ModuleManager to use for module-related operations.
     * @param authenticator   the AuthenticationHandler to use for authentication.
     */
    public GradeController(SemesterManager semesterManager, ModuleManager moduleManager, GradeManager gradeManager, AuthenticationHandler authenticator) {
        this.semesterManager    = semesterManager;
        this.moduleManager      = moduleManager;
        this.gradeManager       = gradeManager;
        this.authenticator      = authenticator;
    }

    /**
     * Handles GET requests to retrieve grades by degree ID.
     *
     * @param context the request context.
     * @return the HTTP response.
     */
    @Route(path = "{degreeId}/grades")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getGradesByDegreeId(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse();

            final Optional<Long> degreeIdOpt = context.getUrlCaptures().get("degreeId").flatMap(LongUtils::tryParseLong);
            if (degreeIdOpt.isPresent()) {
                final List<Semester> semesters = semesterManager.getSemestersForDegree(degreeIdOpt.get());
                final List<SemesterGrade> semesterGrades = semesters.stream().map(semester -> {
                    final List<Module> modules = moduleManager.getModulesBySemester(semester.getId());
                    final List<ModuleGrade> moduleGrades = modules.stream().map(module -> {
                        final List<Grade> grades = gradeManager.getGradesByModule(module.getId());
                        return new ModuleGrade(module.getId(), module.getName(), grades, module.getECTS());
                    }).toList();
                    return new SemesterGrade(semester.getId(), semester.getName(), moduleGrades);
                }).toList();

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
    @Route(path = "{degreeId}/grades")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse updateGradesByDegreeId(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            context.getUrlCaptures().get("degreeId")
                    .flatMap(LongUtils::tryParseLong)
                    .flatMap(degreeId -> request.getRequestBody().flatMap(
                            body -> body.tryRead(ModuleGrade.class))
                    ).ifPresent(moduleGrade -> {
                        final List<Grade> newGrades = moduleGrade.getGrades();
                        if (validateGrades(newGrades)) {
                            gradeManager.updateGradesByModule(moduleGrade.getId(), newGrades);
                            response.setStatusCode(HttpStatusCode.OK);
                        }
                    });
            return response;
        });
    }

    /**
     * Handles GET requests to retrieve the average grade value by degree ID.
     *
     * @param context the request context.
     * @return the HTTP response.
     */
    @Route(path = "{degreeId}/grades/average")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getGradesAveragesByDegreeId(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            context.getUrlCaptures().get("degreeId").flatMap(LongUtils::tryParseLong)
                    .ifPresent(degreeId -> {
                        List<Double> moduleAverageGrades = new ArrayList<>();

                        for (Semester semester : semesterManager.getSemestersForDegree(degreeId)) {
                            for (Module module : moduleManager.getModulesBySemester(semester.getId())) {
                                final List<Grade> grades = gradeManager.getGradesByModule(module.getId());

                                double denominator = 0;
                                double numerator = 0;
                                for (Grade grade : grades) {
                                    numerator += grade.getPercentage() * grade.getValue();
                                    denominator += grade.getPercentage();
                                }
                                moduleAverageGrades.add(numerator / denominator);
                            }
                        }

                        response.setResponseBody(
                                JsonContent.writableOf(
                                        Map.of(
                                                "average",
                                                moduleAverageGrades.stream().mapToDouble(Double::doubleValue).average().orElse(0))
                                ))
                                .setStatusCode(HttpStatusCode.OK);
                    });
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
        return totalPercentage <= 1.0;
    }
}