package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.ModuleDeo;
import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.domain.curriculum.impls.ModuleManagerImpl;
import ch.zhaw.studyflow.services.persistence.ModuleDao;
import ch.zhaw.studyflow.utils.LongUtils;
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
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Controller for handling module-related HTTP requests.
 */
@Route(path = "api/modules")
public class ModuleController {
    private static Logger LOGGER = Logger.getLogger(ModuleController.class.getName());

    private final AuthenticationHandler authenticator;
    private final ModuleManager moduleManager;
    private final PrincipalProvider principalProvider;

    /**
     * Constructs a ModuleController with the specified dependencies.
     *
     * @param moduleManager the module manager
     * @param authenticator the authentication handler
     */
    public ModuleController(ModuleManager moduleManager, AuthenticationHandler authenticator, PrincipalProvider principalProvider) {
        this.authenticator = authenticator;
        this.moduleManager = moduleManager;
        this.principalProvider = principalProvider;
    }

    /**
     * Endpoint for adding a new module.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse addModule(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = context.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            if(request.getRequestBody().isPresent()) {
                Optional<ModuleDeo> optionalModuleDeo = request.getRequestBody()
                        .flatMap(body -> body.tryRead(ModuleDeo.class));

                if(optionalModuleDeo.isPresent() && optionalModuleDeo.get().isValid()) {
                    if(moduleManager.getModuleByName(optionalModuleDeo.get().getName()).isPresent()) {
                        response.setStatusCode(HttpStatusCode.CONFLICT);
                    } else {
                        optionalModuleDeo.map(obj -> {
                            Module module = new Module();
                            module.setName(obj.getName());
                            module.setDescription(obj.getDescription());
                            module.setECTS(obj.getEcts());
                            module.setSemesterId(obj.getSemesterId());
                            module.setImportanceValue(obj.getImportanceValue());
                            module.setTimeValue(obj.getTimeValue());
                            module.setUnderstandingValue(obj.getUnderstandingValue());
                            return module;
                        }).ifPresentOrElse(module -> {
                                    moduleManager.create(module, optionalModuleDeo.get().getSemesterId(), optionalModuleDeo.get().getDegreeId(), principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf).orElseThrow());
                                    response.setStatusCode(HttpStatusCode.CREATED);
                                },
                                () -> {
                                    response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                                });
                    }
                }
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
    @Route(path = "")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getModules(RequestContext context) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            HttpResponse response = context.getRequest().createResponse();
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            if (userId.isPresent()) {
                    List<Module> modules = moduleManager.getModules(userId.get());
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
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            if (userId.isPresent()) {
                    context.getUrlCaptures().get("moduleId").flatMap(LongUtils::tryParseLong).ifPresent(moduleId -> {
                        Module module = moduleManager.read(moduleId);
                        response.setResponseBody(JsonContent.writableOf(module))
                                .setStatusCode(HttpStatusCode.OK);
                    });
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
    @Route(path = "{id}")
    @Endpoint(method = HttpMethod.PATCH)
    public HttpResponse updateModule(RequestContext context) {
        final HttpRequest request = context.getRequest();
        return authenticator.handleIfAuthenticated(request, principal -> {
            HttpResponse response = context.getRequest().createResponse().setStatusCode(HttpStatusCode.BAD_REQUEST);
            context.getUrlCaptures().get("id").flatMap(LongUtils::tryParseLong).ifPresent(moduleId -> {
                request.getRequestBody().flatMap(body -> body.tryRead(ModuleDeo.class))
                        .ifPresent(moduleDeo -> {
                            if (moduleDeo.isValid()) {
                                moduleManager.getModule(moduleId).ifPresentOrElse(module -> {
                                    module.setName(moduleDeo.getName());
                                    module.setDescription(moduleDeo.getDescription());
                                    module.setECTS(moduleDeo.getEcts());
                                    module.setSemesterId(moduleDeo.getSemesterId());
                                    module.setImportanceValue(moduleDeo.getImportanceValue());
                                    module.setTimeValue(moduleDeo.getTimeValue());
                                    module.setUnderstandingValue(moduleDeo.getUnderstandingValue());
                                    moduleManager.update(module);
                                    response.setResponseBody(JsonContent.writableOf(module))
                                            .setStatusCode(HttpStatusCode.OK);
                                }, () -> response.setStatusCode(HttpStatusCode.NOT_FOUND));
                            }
                        });
            }
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
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            if (userId.isPresent()) {
                Optional<Long> moduleId = context.getUrlCaptures().get("moduleId").map(Long::valueOf);

                if (moduleId.isPresent()) {
                    moduleManager.delete(moduleId.get());
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