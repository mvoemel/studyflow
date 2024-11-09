package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.LoginRequest;
import ch.zhaw.studyflow.controllers.deo.Registration;
import ch.zhaw.studyflow.domain.student.Student;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

@Route(path = "api/student")
public class StudentController {
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

    @Route(path = "register")
    public HttpResponse register(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        final Principal principal = principalProvider.getPrincipal(request);
        HttpResponse response = requestContext.getRequest().createResponse();
        if (!principal.getClaim(CommonClaims.AUTHENTICATED).orElse(false)
        && request.getRequestBody().isPresent()) {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Registration.class))
                    .map(loginRequest -> {
                        Student student = new Student();
                        student.setEmail(loginRequest.getEmail());
                        student.setPassword(loginRequest.getPassword());
                        student.setUsername(loginRequest.getUsername());
                        return student;
                    })
                    .flatMap(studentManager::register)
                    .map(student -> {
                        principal.addClaim(CommonClaims.AUTHENTICATED, true);
                        principal.addClaim(CommonClaims.USER_ID, student.getId());
                        principalProvider.setPrincipal(response, principal);
                        return response.setStatusCode(HttpStatusCode.OK);
                    })
                    .orElseGet(() -> response.setStatusCode(HttpStatusCode.UNAUTHORIZED));
        }
        return response;
    }

    @Route(path = "login")
    public HttpResponse login(RequestContext requestContext) {
        final HttpRequest request = requestContext.getRequest();

        final Principal principal = principalProvider.getPrincipal(request);
        HttpResponse response = requestContext.getRequest().createResponse();
        if (!principal.getClaim(CommonClaims.AUTHENTICATED).orElse(false)
        && request.getRequestBody().isPresent()) {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(LoginRequest.class))
                    .flatMap(loginRequest -> studentManager.login(loginRequest.getEmail(), loginRequest.getPassword()))
                    .map(student -> {
                        principal.addClaim(CommonClaims.AUTHENTICATED, true);
                        principal.addClaim(CommonClaims.USER_ID, student.getId());
                        principalProvider.setPrincipal(response, principal);
                        return response.setStatusCode(HttpStatusCode.OK);
                    })
                    .orElseGet(() -> response.setStatusCode(HttpStatusCode.UNAUTHORIZED));
        }
        return response;
    }

        @Route(path = "logout")
        public HttpResponse logout (RequestContext requestContext){
            return authenticator.check(requestContext.getRequest(), principal -> {
                principal.addClaim(CommonClaims.AUTHENTICATED, false);
                principal.addClaim(CommonClaims.USER_ID, -1L);

                HttpResponse response = requestContext.getRequest().createResponse();
                principalProvider.setPrincipal(response, principal);
                return response
                        .setStatusCode(HttpStatusCode.OK);
            });
        }
    }
