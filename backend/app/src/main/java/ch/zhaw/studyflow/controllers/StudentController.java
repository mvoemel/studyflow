package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.LoginRequest;
import ch.zhaw.studyflow.controllers.deo.Registration;
import ch.zhaw.studyflow.controllers.deo.StudentDeo;
import ch.zhaw.studyflow.domain.student.Student;
import ch.zhaw.studyflow.domain.student.StudentManager;
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
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

import java.util.Optional;
import java.util.logging.Logger;

@Route(path = "api/student")
public class StudentController {
    private static Logger LOGGER = Logger.getLogger(StudentController.class.getName());

    private final AuthenticationHandler authenticator;
    private final PrincipalProvider principalProvider;
    private final StudentManager studentManager;


    public StudentController(AuthenticationHandler authenticator,
                             PrincipalProvider principalProvider,
                             StudentManager studentManager) {
        this.authenticator = authenticator;
        this.principalProvider = principalProvider;
        this.studentManager = studentManager;
    }


    @Route(path = "me")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse me(RequestContext requestContext) {
        return authenticator.handleIfAuthenticated(requestContext.getRequest(), principal -> {
            HttpResponse response = requestContext.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            principal.getClaim(CommonClaims.USER_ID)
                    .map(studentManager::getStudent)
                    .ifPresentOrElse(
                            student -> response.setResponseBody(JsonContent.writableOf(StudentDeo.of(student.orElse(null)))),
                            () -> LOGGER.warning("No user id found in authenticated principal")
                    );

            response.setStatusCode(HttpStatusCode.OK);
            return response;
        });
    }

    @Route(path = "settings")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse settings(RequestContext requestContext) {
        return authenticator.handleIfAuthenticated(requestContext.getRequest(), principal -> {
            HttpResponse response = requestContext.getRequest().createResponse();
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            if (userId.isPresent()) {
                studentManager.getSettings(userId.get())
                        .ifPresentOrElse(
                                settings -> response
                                        .setResponseBody(JsonContent.writableOf(settings))
                                        .setStatusCode(HttpStatusCode.OK),
                                () -> response.setStatusCode(HttpStatusCode.NOT_FOUND)
                        );
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    @Route(path = "register")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse register(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        final Principal principal = principalProvider.getPrincipal(request);
        HttpResponse response = requestContext.getRequest().createResponse()
                .setStatusCode(HttpStatusCode.BAD_REQUEST);
        if (!principal.getClaim(CommonClaims.AUTHENTICATED).orElse(false)) {
            if (request.getRequestBody().isPresent()) {
                Optional<Student> newStudent = request.getRequestBody()
                        .flatMap(body -> body.tryRead(Registration.class))
                        .map(registration -> {
                            Student student = new Student();
                            student.setEmail(registration.getEmail());
                            student.setPassword(registration.getPassword());
                            student.setLastname(registration.getLastname());
                            student.setFirstname(registration.getFirstname());
                            return student;
                        });

                if (newStudent.isPresent()) {
                    newStudent.flatMap(studentManager::register)
                            .ifPresentOrElse(student -> {
                                        principal.addClaim(CommonClaims.AUTHENTICATED, true);
                                        principal.addClaim(CommonClaims.USER_ID, student.getId());
                                        principalProvider.setPrincipal(response, principal);
                                        response.setStatusCode(HttpStatusCode.CREATED);
                                    },
                                    () -> response.setStatusCode(HttpStatusCode.FORBIDDEN)
                            );
                }
            }
        } else {
            response.setStatusCode(HttpStatusCode.OK);
        }
        return response;
    }

    @Route(path = "login")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse login(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        final Principal principal = principalProvider.getPrincipal(request);
        HttpResponse response = requestContext.getRequest().createResponse();
        if (!principal.getClaim(CommonClaims.AUTHENTICATED).orElse(false)) {
            if (request.getRequestBody().isPresent()) {
                request.getRequestBody()
                        .flatMap(body -> body.tryRead(LoginRequest.class))
                        .flatMap(loginRequest -> studentManager.login(loginRequest.getEmail(), loginRequest.getPassword()))
                        .ifPresentOrElse(student -> {
                                    principal.addClaim(CommonClaims.AUTHENTICATED, true);
                                    principal.addClaim(CommonClaims.USER_ID, student.getId());
                                    principalProvider.setPrincipal(response, principal);
                                    response.setStatusCode(HttpStatusCode.OK);
                                },
                                () -> response.setStatusCode(HttpStatusCode.UNAUTHORIZED)
                        );
            }
        } else {
            response.setStatusCode(HttpStatusCode.OK);
        }
        return response;
    }

    @Route(path = "logout")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse logout(RequestContext requestContext) {
        return authenticator.handleIfAuthenticated(requestContext.getRequest(), principal -> {
            principal.addClaim(CommonClaims.AUTHENTICATED, false);
            principal.addClaim(CommonClaims.USER_ID, -1L);

            HttpResponse response = requestContext.getRequest().createResponse();
            principalProvider.setPrincipal(response, principal);
            return response.setStatusCode(HttpStatusCode.OK);
        });
    }
}
