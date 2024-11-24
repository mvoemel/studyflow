package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.Registration;
import ch.zhaw.studyflow.domain.student.Student;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.Claim;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static ch.zhaw.studyflow.controllers.HttpMockHelpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentControllerTest {
    private AuthenticationHandler authenticationHandler;
    private PrincipalProvider principalProvider;
    private StudentController studentController;
    private StudentManager studentManager;


    @BeforeEach
    void beforeEach() {
        authenticationHandler   = mock(AuthenticationHandler.class);
        principalProvider       = mock(PrincipalProvider.class);
        studentManager          = mock(StudentManager.class);
        studentController       = new StudentController(authenticationHandler, principalProvider, studentManager);
    }

    @Test
    void testRegister() {
        final Registration registration = new Registration();
        registration.setEmail("test@test.ch");
        registration.setPassword("password");
        registration.setUsername("test");

        final HttpRequest request = makeHttpRequest(makeJsonRequestBody(Registration.class, registration));

        final Map<Claim<?>, Object> claims = new HashMap<>();
        final Principal principal = AuthMockHelpers.makePrincipal(claims);

        when(principalProvider.getPrincipal(request)).thenReturn(principal);

        when(studentManager.register(any(Student.class))).then(invocation -> {
            Student target = invocation.getArgument(0);
            target.setId(1L);
            return Optional.of(target);
        });

        HttpResponse response = studentController.register(makeRequestContext(request));
        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        verify(studentManager, times(1)).register(any(Student.class));
        verify(principal, times(1)).addClaim(CommonClaims.USER_ID, 1L);
        verify(principal, times(1)).addClaim(CommonClaims.AUTHENTICATED, true);

        verify(principalProvider, times(1)).setPrincipal(response, principal);
        assertEquals(HttpStatusCode.CREATED, responseStatusCode.getValue());
    }

    @Test
    void testLogin() {

    }

    @ParameterizedTest
    @MethodSource("provideTargets")
    void testAuthorizationTest(Tuple<String, Function<Tuple<StudentController, RequestContext>, HttpResponse>> target) {
        HttpRequest request = makeHttpRequest();
        AuthMockHelpers.configureFailingAuthHandler(authenticationHandler);

        HttpResponse response = target.value2().apply(new Tuple<>(studentController, makeRequestContext(request)));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.UNAUTHORIZED, responseStatusCode.getValue());
    }

    public static Stream<Tuple<String, Function<Tuple<StudentController, RequestContext>, HttpResponse>>> provideTargets() {
        return Stream.of(
                makeTarget("me", ctrl -> ctrl::me),
                makeTarget("logout", ctrl -> ctrl::logout),
                makeTarget("settings", ctrl -> ctrl::settings)
        );
    }

    private static <T> Tuple<String, Function<Tuple<T, RequestContext>, HttpResponse>> makeTarget(String name, Function<T, Function<RequestContext, HttpResponse>> targetInvoker) {
        return new Tuple<>(
                name,
                argument -> targetInvoker.apply(argument.value1()).apply(argument.value2())
        );
    }
}
