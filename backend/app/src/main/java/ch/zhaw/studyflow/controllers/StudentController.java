package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.LoginRequest;
import ch.zhaw.studyflow.controllers.deo.MeResponse;
import ch.zhaw.studyflow.controllers.deo.Registration;
import ch.zhaw.studyflow.controllers.deo.StudentDeo;
import ch.zhaw.studyflow.domain.student.Settings;
import ch.zhaw.studyflow.domain.student.Student;
import ch.zhaw.studyflow.domain.student.StudentManager;
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
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

import javax.swing.text.html.Option;
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


    @Route(path = "{id}")
    @Endpoint(method = HttpMethod.PATCH)
    public HttpResponse updateStudent(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            requestContext.getUrlCaptures().get("id").flatMap(LongUtils::tryParseLong).ifPresent(studentId ->
                    request.getRequestBody().flatMap(body -> body.tryRead(Registration.class))
                            .ifPresentOrElse(registration ->
                                            studentManager.getStudent(studentId).ifPresentOrElse(
                                                    student -> {
                                                        student.setFirstname(registration.getFirstname());
                                                        student.setLastname(registration.getLastname());
                                                        student.setEmail(registration.getEmail());
                                                        student.setPassword(registration.getPassword());
                                                        studentManager.updateStudent(student);
                                                        response.setResponseBody(JsonContent.writableOf(student))
                                                                .setStatusCode(HttpStatusCode.OK);
                                                        },
                                                    () -> response.setStatusCode(HttpStatusCode.NOT_FOUND)
                            ),
                            () -> response.setStatusCode(HttpStatusCode.NOT_FOUND)
                    ));
            return response;
        });
    }


    @Route(path = "me")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse me(RequestContext requestContext) {
        return authenticator.handleIfAuthenticated(requestContext.getRequest(), principal -> {
            HttpResponse response = requestContext.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            Optional<Student> optionalStudent = principal.getClaim(CommonClaims.USER_ID)
                    .flatMap(studentManager::getStudent);

            if (optionalStudent.isPresent()) {
                optionalStudent.flatMap(student -> {
                            Optional<Settings> settings = studentManager.getSettings(student.getId());
                            return settings.map(s -> new MeResponse(StudentDeo.of(student), s));
                        })
                        .map(JsonContent::writableOf)
                        .map(response::setResponseBody)
                        .ifPresentOrElse(
                                r -> response.setStatusCode(HttpStatusCode.OK),
                                () -> {
                                    response.setStatusCode(HttpStatusCode.NOT_FOUND);
                                    LOGGER.warning("Failed to build 'me' for an authenticated user.");
                                }
                        );
            }
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

    @Route(path = "settings/{settingsId}")
    @Endpoint(method = HttpMethod.PATCH)
    public HttpResponse updateSettings(RequestContext requestContext) {
        return authenticator.handleIfAuthenticated(requestContext.getRequest(), principal -> {
            final HttpResponse response = requestContext.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);
            Optional<Long> optionalSettingsId = requestContext.getUrlCaptures().get("settingsId").flatMap(LongUtils::tryParseLong);

            optionalSettingsId.ifPresent(settingsId -> requestContext.getRequest().getRequestBody()
                    .flatMap(body -> body.tryRead(Settings.class))
                    .ifPresentOrElse(settings -> {
                                settings.setId(settingsId);
                                studentManager.updateSettings(settings);
                                response.setStatusCode(HttpStatusCode.OK);
                            },
                            () -> response.setStatusCode(HttpStatusCode.BAD_REQUEST)
                    ));
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
                Optional<Registration> optionalRegistration = request.getRequestBody()
                        .flatMap(body -> body.tryRead(Registration.class));

                if (optionalRegistration.isPresent()) {
                    if (studentManager.getStudentByEmail(optionalRegistration.get().getEmail()).isPresent()) {
                        response.setStatusCode(HttpStatusCode.CONFLICT);
                    } else {
                        optionalRegistration.map(obj -> {
                                    Student student = new Student();
                                    student.setEmail(obj.getEmail());
                                    student.setPassword(obj.getPassword());
                                    student.setLastname(obj.getLastname());
                                    student.setFirstname(obj.getFirstname());
                                    return student;
                                })
                                .flatMap(studentManager::register)
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
