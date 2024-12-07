package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.LoginRequest;
import ch.zhaw.studyflow.controllers.deo.MeResponse;
import ch.zhaw.studyflow.controllers.deo.Registration;
import ch.zhaw.studyflow.domain.student.Settings;
import ch.zhaw.studyflow.domain.student.Student;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
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
import static org.junit.jupiter.api.Assertions.*;
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
    void testUpdateStudent() {
        final Registration registration = new Registration();
        registration.setFirstname("1");
        registration.setLastname("2");
        registration.setEmail("1@2.3");
        registration.setPassword("1234");

        final Student student = getTestStudent();

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());
        when(studentManager.getStudent(1L)).thenReturn(Optional.of(student));

        HttpRequest request = makeHttpRequest(makeJsonRequestBody(Registration.class, registration));

        studentController.updateStudent(makeRequestContext(request, Map.of("id", "1")));

        verify(studentManager, times(1)).getStudent(1L);
        verify(studentManager, times(1)).updateStudent(student);

        assertEquals("1", student.getFirstname());
        assertEquals("2", student.getLastname());
        assertEquals("1@2.3", student.getEmail());
        assertTrue(student.checkPassword("1234"));
    }

    @Test
    void testMe() {
        final Student student = getTestStudent();
        final Settings settings = new Settings();
        settings.setId(1L);
        settings.setGlobalCalendarId(4);
        settings.setActiveDegree(5);

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        when(studentManager.getStudent(1L)).thenReturn(Optional.of(student));
        when(studentManager.getSettings(1L)).thenReturn(Optional.of(settings));

        HttpRequest request = makeHttpRequest();
        HttpResponse response = studentController.me(makeRequestContext(request));

        ArgumentCaptor<HttpStatusCode> statusCodeCaptor = captureResponseCode(response);
        ArgumentCaptor<WritableBodyContent> responseBodyCaptor = captureResponseBody(response);

        verify(studentManager, times(1)).getStudent(1L);
        verify(studentManager, times(1)).getSettings(1L);
        assertEquals(HttpStatusCode.OK, statusCodeCaptor.getValue());
        assertInstanceOf(WritableBodyContent.class, responseBodyCaptor.getValue());

        assertDoesNotThrow(() -> {
            JsonContent bodyContent = (JsonContent)responseBodyCaptor.getValue();
            MeResponse result = JsonContentHelpers.getContent(bodyContent, MeResponse.class);
            assertEquals(1L, result.getStudent().getId());
            assertEquals("firstname", result.getStudent().getFirstname());
            assertEquals("lastname", result.getStudent().getLastname());
            assertEquals("test@test.test", result.getStudent().getEmail());

            assertEquals(1L, result.getSettings().getId());
            assertEquals(4, result.getSettings().getGlobalCalendarId());
            assertEquals(5, result.getSettings().getActiveDegree());
        });
    }

    @Test
    void testRegister() {
        final Registration registration = new Registration();
        registration.setEmail("test@test.ch");
        registration.setPassword("password");
        registration.setFirstname("firstname");
        registration.setLastname("lastname");

        final HttpRequest request = makeHttpRequest(makeJsonRequestBody(Registration.class, registration));

        final Principal principal = AuthMockHelpers.configureSuccessfulAuthHandler(
                authenticationHandler,
                AuthMockHelpers.getDefaultClaims()
        );

        when(principalProvider.getPrincipal(request)).thenReturn(principal);

        when(studentManager.register(any(Student.class))).then(invocation -> {
            Student target = invocation.getArgument(0);
            target.setId(1L);
            return Optional.of(target);
        });

        HttpResponse response = studentController.register(makeRequestContext(request));
        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        verify(studentManager, times(1)).register(any(Student.class));

        assertEquals(HttpStatusCode.CREATED, responseStatusCode.getValue());
    }

    @Test
    void testRegisterConflict() {
        final Registration registration = new Registration();
        registration.setEmail("test@test.test");
        registration.setPassword("password");
        registration.setLastname("lasname");
        registration.setFirstname("firstname");

        final HttpRequest request = makeHttpRequest(makeJsonRequestBody(Registration.class, registration));

        final Map<Claim<?>, Object> claims = new HashMap<>();
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, claims);

        when(studentManager.getStudentByEmail(registration.getEmail())).thenReturn(Optional.of(new Student()));

        HttpResponse response = studentController.register(makeRequestContext(request));

        verify(studentManager, never()).register(any(Student.class));
        verify(studentManager, atLeast(1)).getStudentByEmail(registration.getEmail());
        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.CONFLICT, responseStatusCode.getValue());
    }

    @Test
    void testRegisterWhenAlreadyAuthenticated() {
        final Registration registration = new Registration();
        registration.setEmail("test@test.test");
        registration.setPassword("password");
        registration.setLastname("lasname");
        registration.setFirstname("firstname");

        final HttpRequest request = makeHttpRequest(makeJsonRequestBody(Registration.class, registration));

        AuthMockHelpers.configureFailingAuthHandler(authenticationHandler);

        HttpResponse response = studentController.register(makeRequestContext(request));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        verify(studentManager, never()).register(any(Student.class));
        verify(studentManager, never()).getStudentByEmail(registration.getEmail());
        assertEquals(HttpStatusCode.UNAUTHORIZED, responseStatusCode.getValue());
    }

    @Test
    void testInvalidLogin() {
        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.test");
        loginRequest.setPassword("password");

        final HttpRequest request = makeHttpRequest(makeJsonRequestBody(LoginRequest.class, loginRequest));

        final Map<Claim<?>, Object> claims = new HashMap<>();
        Principal principal = AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, claims);

        when(principalProvider.getPrincipal(request)).thenReturn(principal);
        when(studentManager.login(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(Optional.empty());

        HttpResponse response = studentController.login(makeRequestContext(request));
        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        verify(studentManager, times(1)).login(loginRequest.getEmail(), loginRequest.getPassword());
        verify(principal, never()).addClaim(eq(CommonClaims.USER_ID), eq(1L));
        verify(principalProvider, never()).setPrincipal(response, principal);
        assertEquals(HttpStatusCode.UNAUTHORIZED, responseStatusCode.getValue());
    }

    @Test
    void testSuccessfulLogin() {
        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.test");
        loginRequest.setPassword("password");

        final Student student = getTestStudent();

        final HttpRequest request = makeHttpRequest(makeJsonRequestBody(LoginRequest.class, loginRequest));

        final Map<Claim<?>, Object> claims = new HashMap<>();
        final Principal principal = AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, claims);

        when(principalProvider.getPrincipal(request)).thenReturn(principal);
        when(studentManager.login(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(Optional.of(student));

        HttpResponse response = studentController.login(makeRequestContext(request));
        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        verify(studentManager, times(1)).login(loginRequest.getEmail(), loginRequest.getPassword());

        verify(principal, times(1)).addClaim(eq(CommonClaims.USER_ID), eq(1L));
        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());
    }

    @Test
    void testLogout() {
        final HttpRequest request = makeHttpRequest();

        Principal principal = AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());
        HttpResponse response = studentController.logout(makeRequestContext(request));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        verify((principalProvider), times(1)).clearPrincipal(response);
        assertEquals(0, principal.getClaims().size());
        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());
    }

    @Test
    void testGetSettings() {
        final HttpRequest request = makeHttpRequest();

        Settings settings = new Settings();
        settings.setId(1L);
        settings.setGlobalCalendarId(1);
        settings.setActiveDegree(1);

        when(studentManager.getSettings(1L)).thenReturn(Optional.of(settings));

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());
        HttpResponse response = studentController.getSettings(makeRequestContext(request));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        ArgumentCaptor<WritableBodyContent> responseBody = captureResponseBody(response);
        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());
        assertInstanceOf(JsonContent.class, responseBody.getValue());
        assertDoesNotThrow(() -> {
            JsonContent content = (JsonContent)responseBody.getValue();
            Settings result = JsonContentHelpers.getContent(content, Settings.class);
            assertEquals(1L, result.getId());
            assertEquals(1, result.getGlobalCalendarId());
            assertEquals(1, result.getActiveDegree());
        });
    }

    @Test
    void testUpdateSettings() {
        final Settings settings = new Settings();
        settings.setId(1L);
        settings.setGlobalCalendarId(1);
        settings.setActiveDegree(1);

        final HttpRequest request = makeHttpRequest(makeJsonRequestBody(Settings.class, settings));

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());
        HttpResponse response = studentController.updateSettings(makeRequestContext(request, Map.of("settingsId", "1")));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        verify(studentManager, times(1)).updateSettings(settings);
        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());
    }

    private static Student getTestStudent() {
        final Student student = new Student();
        student.setId(1L);
        student.setSettingsId(1L);
        student.setFirstname("firstname");
        student.setLastname("lastname");
        student.setEmail("test@test.test");
        student.setPassword("password");
        return student;
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
                makeTarget("settings", ctrl -> ctrl::getSettings)
        );
    }

    private static <T> Tuple<String, Function<Tuple<T, RequestContext>, HttpResponse>> makeTarget(String name, Function<T, Function<RequestContext, HttpResponse>> targetInvoker) {
        return new Tuple<>(
                name,
                argument -> targetInvoker.apply(argument.value1()).apply(argument.value2())
        );
    }
}
