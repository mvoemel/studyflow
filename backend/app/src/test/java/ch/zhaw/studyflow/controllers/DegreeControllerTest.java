package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.domain.curriculum.DegreeManager;
import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
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

class DegreeControllerTest {
    private AuthenticationHandler authenticationHandler;
    private DegreeManager degreeManager;
    private DegreeController degreeController;


    @BeforeEach
    void beforeEach() {
        this.authenticationHandler  = mock(AuthenticationHandler.class);
        this.degreeManager          = mock(DegreeManager.class);
        this.degreeController       = new DegreeController(authenticationHandler, degreeManager);
    }


    @Test
    void testCreateDegree() {
        Degree degree = new Degree();
        degree.setName("Test Degree");
        degree.setDescription("Test Description");

        HttpRequest request = makeHttpRequest(makeJsonRequestBody(Degree.class, degree));


        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());
        HttpResponse response = degreeController.createDegree(makeRequestContext(request));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);

        assertEquals(HttpStatusCode.CREATED, responseStatusCode.getValue());

        verify(degreeManager, times(1)).createDegree(1L, degree);
    }

    @Test
    void testGetDegree() {
        HttpRequest request = makeHttpRequest();
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        when(degreeManager.getDegree(1)).thenReturn(makeDegree(1, 1));

        final RequestContext requestContext = makeRequestContext(request, Map.of("degreeId", "1"));

        HttpResponse response = degreeController.getDegree(requestContext);

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        ArgumentCaptor<WritableBodyContent> responseBody = captureResponseBody(response);

        assertInstanceOf(JsonContent.class, responseBody.getValue());
        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());
        // TODO find a better way to test if the body content is also equals
        //      to the expected degree.
        verify(degreeManager, times(1)).getDegree(1);
    }

    @Test
    void testDegrees() {
        when(degreeManager.getDegreesForStudent(1)).thenReturn(
                List.of(
                        makeDegree(1, 0),
                        makeDegree(1, 1),
                        makeDegree(1, 2)
                )
        );

        HttpRequest request = makeHttpRequest();
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        HttpResponse response = degreeController.getDegrees(makeRequestContext(request));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());

        verify(degreeManager, times(1)).getDegreesForStudent(1);
    }

    @Test
    void testDeleteDegree() {
        Degree degree = new Degree();
        degree.setId(1);
        degree.setOwnerId(1);
        degree.setName("Test Degree");
        degree.setDescription("Test Description");

        when(degreeManager.getDegree(1)).thenReturn(degree);

        HttpRequest request = makeHttpRequest();
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        RequestContext context = makeRequestContext(request, Map.of("degreeId", "1"));

        HttpResponse response = degreeController.deleteDegree(context);

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.NO_CONTENT, responseStatusCode.getValue());

        verify(degreeManager, times(1)).deleteDegree(1);
    }

    @ParameterizedTest
    @MethodSource("provideTargets")
    void testAuthorizationTest(Tuple<String, Function<Tuple<DegreeController, RequestContext>, HttpResponse>> target) {
        HttpRequest request = makeHttpRequest(makeJsonRequestBody(Degree.class, new Degree()));
        AuthMockHelpers.configureFailingAuthHandler(authenticationHandler);

        HttpResponse response = target.value2().apply(new Tuple<>(degreeController, makeRequestContext(request)));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.UNAUTHORIZED, responseStatusCode.getValue());
    }

    public static Stream<Tuple<String, Function<Tuple<DegreeController, RequestContext>, HttpResponse>>> provideTargets() {
        return Stream.of(
                makeTarget("createDegree", ctrl -> ctrl::createDegree),
                makeTarget("getDegrees", ctrl -> ctrl::getDegrees),
                makeTarget("getDegree", ctrl -> ctrl::getDegree),
                makeTarget("deleteDegree", ctrl -> ctrl::deleteDegree)
        );
    }

    private static <T> Tuple<String, Function<Tuple<T, RequestContext>, HttpResponse>> makeTarget(String name, Function<T, Function<RequestContext, HttpResponse>> targetInvoker) {
        return new Tuple<>(
                name,
                argument -> targetInvoker.apply(argument.value1()).apply(argument.value2())
        );
    }
    
    private Degree makeDegree(int studentId, int degreeId) {
        Degree degree = new Degree();
        degree.setId(degreeId);
        degree.setOwnerId(studentId);
        degree.setName("Test Degree " + degreeId);
        degree.setDescription("Test Description " + degreeId);
        return degree;

    }
}
