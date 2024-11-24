package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.webserver.annotations.Endpoint;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.domain.curriculum.Module;
import java.util.List;
import java.util.Optional;

/**
 * Controller for handling module-related HTTP requests.
 */
@Route(path = "api/module")
public class ModuleController {

    private final AuthenticationHandler authenticator;
    private final ModuleManager moduleManager;

    /**
     * Constructs a ModuleController with the specified dependencies.
     *
     * @param moduleManager the module manager
     * @param authenticator the authentication handler
     */
    public ModuleController(ModuleManager moduleManager, AuthenticationHandler authenticator) {
        this.authenticator = authenticator;
        this.moduleManager = moduleManager;
    }

    /**
     * Endpoint for adding a new module.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse addModule(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Module.class))
                    .flatMap(module -> {
                        moduleManager.create(module);
                        return Optional.of(module);
                    })
                    .ifPresentOrElse(
                            module -> response.setResponseBody(JsonContent.writableOf(module))
                                    .setStatusCode(HttpStatusCode.CREATED),
                            () -> response.setStatusCode(HttpStatusCode.BAD_REQUEST)
                    );
            return response;
        });
    }

    /**
     * Endpoint for retrieving modules by degree ID and semester ID.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{degreeId}/{semesterId}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getModules(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                try {
                    long degreeId = Long.parseLong(context.getUrlCaptures().get("degreeId").orElseThrow(() -> new IllegalArgumentException("Missing degreeId")));
                    long semesterId = Long.parseLong(context.getUrlCaptures().get("semesterId").orElseThrow(() -> new IllegalArgumentException("Missing semesterId")));
                    List<Module> modules = moduleManager.getModules(userId.get(), degreeId, semesterId);
                    response.setResponseBody(JsonContent.writableOf(modules))
                            .setStatusCode(HttpStatusCode.OK);
                } catch (IllegalArgumentException e) {
                    response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                    e.printStackTrace(); // Log the exception
                } catch (Exception e) {
                    response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
                    e.printStackTrace(); // Log the exception
                }
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    /**
     * Endpoint for retrieving a specific module by degree ID, semester ID, and module ID.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{degreeId}/{semesterId}/{moduleId}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getModule(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                try {
                    long degreeId = Long.parseLong(context.getUrlCaptures().get("degreeId").orElseThrow(() -> new IllegalArgumentException("Missing degreeId")));
                    long semesterId = Long.parseLong(context.getUrlCaptures().get("semesterId").orElseThrow(() -> new IllegalArgumentException("Missing semesterId")));
                    long moduleId = Long.parseLong(context.getUrlCaptures().get("moduleId").orElseThrow(() -> new IllegalArgumentException("Missing moduleId")));
                    Module module = moduleManager.getModule(userId.get(), degreeId, semesterId, moduleId);
                    response.setResponseBody(JsonContent.writableOf(module))
                            .setStatusCode(HttpStatusCode.OK);
                } catch (NumberFormatException e) {
                    response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                }
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    /**
     * Endpoint for updating a specific module.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{degreeId}/{semesterId}/{moduleId}")
    @Endpoint(method = HttpMethod.PUT)
    public HttpResponse updateModule(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Module.class))
                    .flatMap(module -> {
                        moduleManager.update(module);
                        return Optional.of(module);
                    })
                    .ifPresentOrElse(
                            module -> response.setResponseBody(JsonContent.writableOf(module))
                                    .setStatusCode(HttpStatusCode.OK),
                            () -> response.setStatusCode(HttpStatusCode.BAD_REQUEST)
                    );
            return response;
        });
    }

    /**
     * Endpoint for deleting a specific module.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{degreeId}/{semesterId}/{moduleId}")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteModule(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                Optional<Long> degreeId = principal.getClaim(CommonClaims.DEGREE_ID).map(Long::valueOf);
                Optional<Long> semesterId = principal.getClaim(CommonClaims.SEMESTER_ID).map(Long::valueOf);
                Optional<Long> moduleId = principal.getClaim(CommonClaims.MODULE_ID).map(Long::valueOf);

                if (degreeId.isPresent() && semesterId.isPresent() && moduleId.isPresent()) {
                    moduleManager.delete(userId.get(), moduleId.get());
                    response.setStatusCode(HttpStatusCode.NO_CONTENT);
                } else {
                    response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                }
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }
}