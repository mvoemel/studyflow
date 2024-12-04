package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.domain.curriculum.DegreeManager;
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

import java.util.Optional;
import java.util.logging.Logger;

@Route(path = "api/degrees")
public class DegreeController {
    private static final Logger LOGGER = Logger.getLogger(DegreeController.class.getName());

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
            final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            principal.getClaim(CommonClaims.USER_ID)
                    .flatMap(userId -> request.getRequestBody()
                            .flatMap(body -> body.tryRead(Degree.class))
                            .map(degree -> {
                                degreeManager.createDegree(userId, degree);
                                return degree;
                            }))
                    .ifPresent(value ->
                            response.setResponseBody(JsonContent.writableOf(value))
                                    .setStatusCode(HttpStatusCode.CREATED)
                    );
            return response;
        });
    }

    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getDegrees(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            principal.getClaim(CommonClaims.USER_ID)
                    .ifPresent(userId ->
                            response.setResponseBody(JsonContent.writableOf(degreeManager.getDegreesForStudent(userId)))
                                    .setStatusCode(HttpStatusCode.OK)
                    );
            return response;
        });
    }

    @Route(path = "{degreeId}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getDegree(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            final Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            final Optional<Long> degreeId = requestContext.getUrlCaptures().get("degreeId").flatMap(LongUtils::tryParseLong);
            if (userId.isPresent() && degreeId.isPresent()) {
                final Degree requestedDegree = degreeManager.getDegree(degreeId.get());
                if (requestedDegree != null) {
                    response.setResponseBody(JsonContent.writableOf(requestedDegree))
                            .setStatusCode(HttpStatusCode.OK);
                }
            }
            return response;
        });
    }

    @Route(path = "{degreeId}")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse updateDegree(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            final Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            final Optional<Long> degreeId = requestContext.getUrlCaptures().get("degreeId").flatMap(LongUtils::tryParseLong);
            if (userId.isPresent() && degreeId.isPresent()) {
                request.getRequestBody()
                        .flatMap(body -> body.tryRead(Degree.class))
                        .ifPresent(
                                degree -> {
                                    if (degree.getId() == degreeId.get()) {
                                        degreeManager.updateDegree(degree);
                                        response.setResponseBody(JsonContent.writableOf(degree));
                                        response.setStatusCode(HttpStatusCode.OK);
                                    } else {
                                        LOGGER.warning("Ignoring degree update due to mismatching degree ID.");
                                    }
                                }
                        );
            }
            return response;
        });
    }

    @Route(path = "{degreeId}")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteDegree(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        return authenticationHandler.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            final Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            final Optional<Long> degreeId = requestContext.getUrlCaptures().get("degreeId").flatMap(LongUtils::tryParseLong);
            if (userId.isPresent() && degreeId.isPresent()) {
                degreeManager.deleteDegree(degreeId.get());
                response.setStatusCode(HttpStatusCode.NO_CONTENT);
            }
            return response;
        });
    }
}
