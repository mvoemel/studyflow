package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.domain.curriculum.impls.ModuleManagerImpl;
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
@Route(path = "api/modules")
public class ModuleController {

    private final AuthenticationHandler authenticator;
    private final ModuleManager moduleManagerImpl;

    /**
     * Constructs a ModuleController with the specified dependencies.
     *
     * @param moduleManagerImpl the module manager
     * @param authenticator the authentication handler
     */
    public ModuleController(ModuleManager moduleManagerImpl, AuthenticationHandler authenticator) {
        this.authenticator = authenticator;
        this.moduleManagerImpl = moduleManagerImpl;
    }

    /**
     * Endpoint for adding a new module.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "degrees/{degreeId}/semesters/{semesterId}")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse addModule(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            long semesterId = Long.parseLong(context.getUrlCaptures().get("semesterId").orElseThrow(() -> new IllegalArgumentException("Missing semesterId")));

            if(userId.isPresent()) {
                    request.getRequestBody()
                            .flatMap(body -> body.tryRead(Module.class))
                            .flatMap(module -> {
                                moduleManagerImpl.create(module, semesterId);
                                return Optional.of(module);
                            })
                            .ifPresentOrElse(
                                    module -> response.setResponseBody(JsonContent.writableOf(module))
                                            .setStatusCode(HttpStatusCode.CREATED),
                                    () -> response.setStatusCode(HttpStatusCode.BAD_REQUEST)
                            );
            }
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
                    long degreeId = Long.parseLong(context.getUrlCaptures().get("degreeId").orElseThrow(() -> new IllegalArgumentException("Missing degreeId")));
                    long semesterId = Long.parseLong(context.getUrlCaptures().get("semesterId").orElseThrow(() -> new IllegalArgumentException("Missing semesterId")));
                    List<Module> modules = moduleManagerImpl.getModules(userId.get(), degreeId, semesterId);
                    response.setResponseBody(JsonContent.writableOf(modules))
                            .setStatusCode(HttpStatusCode.OK);
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
    @Route(path = "{moduleId}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getModule(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                    long moduleId = Long.parseLong(context.getUrlCaptures().get("moduleId").orElseThrow(() -> new IllegalArgumentException("Missing moduleId")));
                    Module module = moduleManagerImpl.getModule(moduleId);
                    response.setResponseBody(JsonContent.writableOf(module))
                            .setStatusCode(HttpStatusCode.OK);
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
    @Route(path = "")
    @Endpoint(method = HttpMethod.PUT)
    public HttpResponse updateModule(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Module.class))
                    .ifPresentOrElse(
                            module -> {
                                moduleManagerImpl.update(module);
                                response.setStatusCode(HttpStatusCode.OK);
                            },
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
    @Route(path = "{moduleId}")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteModule(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                Optional<Long> moduleId = context.getUrlCaptures().get("moduleId").map(Long::valueOf);

                if (moduleId.isPresent()) {
                    moduleManagerImpl.delete(moduleId.get());
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