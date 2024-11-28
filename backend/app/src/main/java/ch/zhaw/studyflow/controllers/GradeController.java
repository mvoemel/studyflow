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
import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Route(path = "api/degrees")
public class GradeController {
    private static final Logger logger = Logger.getLogger(GradeController.class.getName());
    private final GradeDao gradeDao;
    private final AuthenticationHandler authenticator;

    public GradeController(GradeDao gradeDao, AuthenticationHandler authenticator) {
        this.gradeDao = gradeDao;
        this.authenticator = authenticator;
    }

    @Route(path = "{degreeId}/grades")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getGradesByDegreeId(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<String> degreeIdOpt = context.getUrlCaptures().get("degreeId");
            if (degreeIdOpt.isPresent()) {
                long degreeId = Long.parseLong(degreeIdOpt.get());
                List<Grade> grades = gradeDao.readByDegree(degreeId);
                Map<Long, Map<Long, List<Grade>>> semesters = grades.stream()
                        .collect(Collectors.groupingBy(
                                grade -> getFieldValue(grade, "semesterId"),
                                Collectors.groupingBy(grade -> getFieldValue(grade, "moduleId"))
                        ));
                response.setResponseBody(JsonContent.writableOf(semesters))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    @Route(path = "{degreeId}/grades")
    @Endpoint(method = HttpMethod.PATCH)
    public HttpResponse patchGradesByDegreeId(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<String> degreeIdOpt = context.getUrlCaptures().get("degreeId");
            if (degreeIdOpt.isPresent()) {
                long degreeId = Long.parseLong(degreeIdOpt.get());
                request.getRequestBody()
                        .flatMap(body -> body.tryRead(Map.class))
                        .ifPresentOrElse(
                                moduleData -> {
                                    Map<String, Object> moduleDataMap = (Map<String, Object>) moduleData;
                                    long moduleId = ((Number) moduleDataMap.get("id")).longValue();
                                    List<Map<String, Object>> gradesData = (List<Map<String, Object>>) moduleDataMap.get("grades");
                                    List<Grade> grades = gradesData.stream().map(data -> {
                                        Grade grade = new Grade();
                                        grade.setId(((Number) data.get("id")).longValue());
                                        grade.setName((String) data.get("name"));
                                        grade.setPercentage(((Number) data.get("percentage")).doubleValue());
                                        grade.setValue(((Number) data.get("value")).doubleValue());
                                        return grade;
                                    }).collect(Collectors.toList());
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

    @Route(path = "{degreeId}/grades/averages")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getGradesAveragesByDegreeId(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<String> degreeIdOpt = context.getUrlCaptures().get("degreeId");
            if (degreeIdOpt.isPresent()) {
                long degreeId = Long.parseLong(degreeIdOpt.get());
                List<Grade> grades = gradeDao.readByDegree(degreeId);
                Map<Long, Map<Long, List<Grade>>> semesters = grades.stream()
                        .collect(Collectors.groupingBy(
                                grade -> getFieldValue(grade, "semesterId"),
                                Collectors.groupingBy(grade -> getFieldValue(grade, "moduleId"))
                        ));
                Map<Long, Map<Long, Double>> averages = semesters.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().entrySet().stream()
                                        .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                e -> e.getValue().stream().mapToDouble(Grade::getValue).average().orElse(0.0)
                                        ))
                        ));
                response.setResponseBody(JsonContent.writableOf(averages))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    private long getFieldValue(Grade grade, String fieldName) {
        try {
            Field field = Grade.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getLong(grade);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateGrades(List<Grade> grades) {
        double totalPercentage = grades.stream().mapToDouble(Grade::getPercentage).sum();
        return totalPercentage == 1.0;
    }
}