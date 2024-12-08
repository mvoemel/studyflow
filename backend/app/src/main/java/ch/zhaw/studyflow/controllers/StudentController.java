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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        this.authenticator      = authenticator;
        this.principalProvider  = principalProvider;
        this.studentManager     = studentManager;
    }


    @Route(path = "{id}")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse updateStudent(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            requestContext.getUrlCaptures().get("id").flatMap(LongUtils::tryParseLong).ifPresent(studentId ->
                    request.getRequestBody().flatMap(body -> body.tryRead(Registration.class))
                            .ifPresent(registration -> {
                                if (registration.isValid()) {
                                    studentManager.getStudent(studentId).ifPresentOrElse(
                                            student -> {
                                                student.setFirstname(registration.getFirstname());
                                                student.setLastname(registration.getLastname());
                                                student.setEmail(registration.getEmail());
                                                if (registration.getPassword() != null) {
                                                    student.setPassword(registration.getPassword());
                                                }
                                                try {
                                                    studentManager.updateStudent(student);
                                                    response.setResponseBody(JsonContent.writableOf(student))
                                                            .setStatusCode(HttpStatusCode.OK);
                                                } catch (IllegalArgumentException e) {
                                                    response.setStatusCode(HttpStatusCode.CONFLICT);
                                                }

                                            },
                                            () -> response.setStatusCode(HttpStatusCode.NOT_FOUND)
                                    );
                                }
                            }));
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
    public HttpResponse getSettings(RequestContext requestContext) {
        return authenticator.handleIfAuthenticated(requestContext.getRequest(), principal -> {
            HttpResponse response = requestContext.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);
            principal.getClaim(CommonClaims.USER_ID).ifPresent(userId ->
                    studentManager.getSettings(userId)
                            .ifPresentOrElse(
                                    settings -> response
                                            .setResponseBody(JsonContent.writableOf(settings))
                                            .setStatusCode(HttpStatusCode.OK),
                                    () -> response.setStatusCode(HttpStatusCode.NOT_FOUND)
                            )
            );
            return response;
        });
    }

    @Route(path = "settings/{settingsId}")
    @Endpoint(method = HttpMethod.POST)
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

        return authenticator.handleIfUnauthenticated(request, principal -> {
            HttpResponse response = requestContext.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            if (request.getRequestBody().isPresent()) {
                Optional<Registration> optionalRegistration = request.getRequestBody()
                        .flatMap(body -> body.tryRead(Registration.class));

                if (optionalRegistration.isPresent() && optionalRegistration.get().isValid()) {
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
                                            response.setResponseBody(JsonContent.writableOf("Successfully registered"))
                                                    .setStatusCode(HttpStatusCode.CREATED);
                                        },
                                        () -> response.setStatusCode(HttpStatusCode.FORBIDDEN)
                                );
                    }
                }
            }
            return response;
        });
    }

    @Route(path = "login")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse login(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        return authenticator.handleIfUnauthenticated(request, principal -> {
            final HttpResponse response = requestContext.getRequest().createResponse();
            if (request.getRequestBody().isPresent()) {
                request.getRequestBody()
                        .flatMap(body -> body.tryRead(LoginRequest.class))
                        .flatMap(loginRequest -> studentManager.login(loginRequest.getEmail(), loginRequest.getPassword()))
                        .ifPresentOrElse(student -> {
                                    setLoggedinPrincipalClaims(principal, student);
                                    response.setResponseBody(JsonContent.writableOf("Successfully logged in"))
                                            .setStatusCode(HttpStatusCode.OK);
                                },
                                () -> response.setStatusCode(HttpStatusCode.UNAUTHORIZED)
                        );
            }
            return response;
        });
    }

    private static void setLoggedinPrincipalClaims(Principal principal, Student student) {
        principal.addClaim(CommonClaims.USER_ID, student.getId());
        principal.addClaim(CommonClaims.EMAIL, student.getEmail());
        principal.addClaim(CommonClaims.SETTINGS_ID, student.getSettingsId());
        principal.addClaim(CommonClaims.EXPIRES, LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    @Route(path = "logout")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse logout(RequestContext requestContext) {
        return authenticator.handleIfAuthenticated(requestContext.getRequest(), principal -> {
            HttpResponse response = requestContext.getRequest().createResponse();
            principal.clearClaims();
            principalProvider.clearPrincipal(response);
            return response.setStatusCode(HttpStatusCode.OK);
        });
    }
}
