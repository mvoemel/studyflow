package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.SemesterDeo;
import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static ch.zhaw.studyflow.controllers.HttpMockHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SemesterControllerTest {
    private AuthenticationHandler authenticationHandler;
    private SemesterManager semesterManager;
    private SemesterController semesterController;


    @BeforeEach
    void beforeEach() {
        this.authenticationHandler  = mock(AuthenticationHandler.class);
        this.semesterManager        = mock(SemesterManager.class);
        this.semesterController       = new SemesterController(authenticationHandler, semesterManager);
    }


    @Test
    void testCreateSemester() {
        SemesterDeo semester = new SemesterDeo();
        semester.setName("Test Semester");
        semester.setDescription("Test Description");
        semester.setUserId(1L);
        semester.setDegreeId(1L);

        HttpRequest request = makeHttpRequest(makeJsonRequestBody(SemesterDeo.class, semester));

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        HttpResponse response = semesterController.createSemester(makeRequestContext(request));
        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);

        assertEquals(HttpStatusCode.CREATED, responseStatusCode.getValue());

        verify(semesterManager).createSemester(any(Semester.class), eq(semester.getDegreeId()), eq(semester.getUserId()));
    }

    @Test
    void testGetSemester() {
        HttpRequest request = makeHttpRequest();
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        when(semesterManager.getSemestersForStudent(1)).thenReturn(List.of(makeSemester(1, 1)));

        final RequestContext requestContext = makeRequestContext(request);

        HttpResponse response = semesterController.getSemesters(requestContext);
        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        ArgumentCaptor<WritableBodyContent> responseBody = captureResponseBody(response);

        assertInstanceOf(JsonContent.class, responseBody.getValue());
        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());

        verify(semesterManager, times(1)).getSemestersForStudent(1);

    }

    @Test
    void testDeleteSemester() {
        Semester semester = makeSemester(1, 1);

        when(semesterManager.getSemesterById(1)).thenReturn(semester);

        HttpRequest request = makeHttpRequest();
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        final RequestContext requestContext = makeRequestContext(request, Map.of("id", "1"));

        HttpResponse response = semesterController.deleteSemester(requestContext);

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());

        verify(semesterManager, times(1)).deleteSemester(1);
    }

    @Test
    void testUpdateSemester() {
        final SemesterDeo semesterDeo = new SemesterDeo();
        semesterDeo.setName("Test Semester");
        semesterDeo.setDescription("Test Description");
        semesterDeo.setUserId(1L);
        semesterDeo.setDegreeId(1L);

        final Semester semester = makeSemester(1, 1);

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());
        when(semesterManager.getSemesterById(1)).thenReturn(semester);

        HttpRequest request = makeHttpRequest(makeJsonRequestBody(SemesterDeo.class, semesterDeo));

        semesterController.updateSemester(makeRequestContext(request, Map.of("id", "1")));

        verify(semesterManager,times(1)).getSemesterById(1);
        verify(semesterManager,times(1)).updateSemester(any(Semester.class));

        assertEquals("Test Semester", semester.getName());
        assertEquals("Test Description", semester.getDescription());

    }

    @ParameterizedTest
    @MethodSource("provideTargets")
    void testAuthorizationTest(Tuple<String, Function<Tuple<SemesterController, RequestContext>, HttpResponse>> target) {
        HttpRequest request = makeHttpRequest(makeJsonRequestBody(Semester.class, new Semester()));
        AuthMockHelpers.configureFailingAuthHandler(authenticationHandler);

        HttpResponse response = target.value2().apply(new Tuple<>(semesterController, makeRequestContext(request)));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.UNAUTHORIZED, responseStatusCode.getValue());
    }


    public static Stream<Tuple<String, Function<Tuple<SemesterController, RequestContext>, HttpResponse>>> provideTargets() {
        return Stream.of(
                makeTarget("createSemester", ctrl -> ctrl::createSemester),
                makeTarget("getSemesters", ctrl -> ctrl::getSemesters),
                makeTarget("deleteSemester", ctrl -> ctrl::deleteSemester)
        );
    }

    private static <T> Tuple<String, Function<Tuple<T, RequestContext>, HttpResponse>> makeTarget(String name, Function<T, Function<RequestContext, HttpResponse>> targetInvoker) {
        return new Tuple<>(
                name,
                argument -> targetInvoker.apply(argument.value1()).apply(argument.value2())
        );
    }

    private Semester makeSemester(long id, long userId) {
        Semester semester = new Semester();
        semester.setId(id);
        semester.setName("Test Semester");
        semester.setDescription("Test Description");
        semester.setUserId(userId);
        semester.setDegreeId(1);
        return semester;
    }
}
