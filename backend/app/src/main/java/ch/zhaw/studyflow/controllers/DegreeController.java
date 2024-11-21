package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.domain.curriculum.DegreeManager;
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

import java.util.Optional;

@Route(path = "/api/degrees")
public class DegreeController {
    private final AuthenticationHandler authenticationHandler;
    private final DegreeManager degreeManager;


    public DegreeController(AuthenticationHandler authenticationHandler, DegreeManager degreeManager) {
        this.authenticationHandler = authenticationHandler;
        this.degreeManager = degreeManager;
    }

    @Endpoint(method = HttpMethod.POST)
    public HttpResponse createDegree(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();
        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            Optional<Integer> userId = principal.getClaim(CommonClaims.USER_ID);

            HttpResponse response = request.createResponse();
            if (userId.isPresent()) {
                request.getRequestBody()
                        .flatMap(body -> body.tryRead(Degree.class))
                        .map(degree -> {
                            degree.setOwnerId(userId.get());
                            degreeManager.createDegree(degree);
                            return degree;
                        }).ifPresentOrElse(
                                value -> response.setResponseBody(JsonContent.writableOf(value)).setStatusCode(HttpStatusCode.CREATED),
                                () -> response.setStatusCode(HttpStatusCode.BAD_REQUEST)
                        );
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getDegrees(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();
        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            Optional<Integer> userId = principal.getClaim(CommonClaims.USER_ID);

            HttpResponse response = request.createResponse();
            if (userId.isPresent()) {
                request.createResponse()
                        .setResponseBody(JsonContent.writableOf(degreeManager.getDegreesForStudent(userId.get())))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    @Route(path = "{degreeId}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getDegree(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();
        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            Optional<Integer> userId = principal.getClaim(CommonClaims.USER_ID);

            Optional<Long> degreeId = requestContext.getUrlCaptures().get("degreeId").map(Long::parseLong);
            HttpResponse response = request.createResponse();
            if (userId.isPresent() && degreeId.isPresent()) {
                final Degree requestedDegree = degreeManager.getDegree(degreeId.get());
                if (requestedDegree != null) {
                    response.setResponseBody(JsonContent.writableOf(requestedDegree))
                            .setStatusCode(HttpStatusCode.OK);
                } else {
                    response.setStatusCode(HttpStatusCode.NOT_FOUND);
                }
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    @Route(path = "{degreeId}")
    @Endpoint(method = HttpMethod.PUT)
    public HttpResponse updateDegree(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();
        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            Optional<Integer> userId = principal.getClaim(CommonClaims.USER_ID);

            Optional<Long> degreeId = requestContext.getUrlCaptures().get("degreeId").map(Long::parseLong);
            HttpResponse response = request.createResponse();
            if (userId.isPresent() && degreeId.isPresent()) {
                request.getRequestBody()
                        .flatMap(body -> body.tryRead(Degree.class))
                        .ifPresentOrElse(
                                degree -> {
                                    try {
                                        degreeManager.updateDegree(degree);
                                        response.setResponseBody(JsonContent.writableOf(degree));
                                        response.setStatusCode(HttpStatusCode.OK);
                                    } catch (IllegalArgumentException e) {
                                        response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                                    }
                                },
                                () -> response.setStatusCode(HttpStatusCode.BAD_REQUEST)
                        );
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    @Route(path = "{degreeId}")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteDegree(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();
        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            Optional<Integer> userId = principal.getClaim(CommonClaims.USER_ID);

            Optional<Long> degreeId = requestContext.getUrlCaptures().get("degreeId").map(Long::parseLong);
            HttpResponse response = request.createResponse();
            if (userId.isPresent() && degreeId.isPresent()) {
                try {
                    Degree degree = degreeManager.getDegree(degreeId.get());
                    degreeManager.deleteDegree(degreeId.get());
                    response.setStatusCode(HttpStatusCode.OK);
                } catch (IllegalArgumentException e) {
                    response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                }
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }
}
