package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.domain.curriculum.DegreeManager;
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
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Map;

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


        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, Map.of(
                CommonClaims.AUTHENTICATED, true,
                CommonClaims.USER_ID, 1)
        );

        HttpResponse response = degreeController.createDegree(makeRequestContext(request));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);

        assertEquals(HttpStatusCode.CREATED, responseStatusCode.getValue());

        verify(degreeManager, times(1)).createDegree(degree);
    }

    @Test
    void testGetDegree() {
        HttpRequest request = makeHttpRequest();
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, Map.of(
                CommonClaims.AUTHENTICATED, true,
                CommonClaims.USER_ID, 1)
        );

        final RequestContext requestContext = makeRequestContext(request, Map.of("degreeId", "1"));

        HttpResponse response = degreeController.getDegree(requestContext);

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        ArgumentCaptor<WritableBodyContent> responseBody = captureResponseBody(response);

        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());

        WritableBodyContent bodyContent = responseBody.getValue();
        assertNotNull(bodyContent);
        assertInstanceOf(JsonContent.class, bodyContent);
        // TODO find a better way to test if the body content is also equals
        //      to the expected degree.
        verify(degreeManager, times(1)).getDegree(1);
    }

    @Test
    void testIllegalGetDegree() {
        Degree degree = makeDegree(6, 5);
        when(degreeManager.getDegree(5)).thenReturn(degree);

        HttpRequest request = makeHttpRequest();
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, Map.of(
                CommonClaims.AUTHENTICATED, true,
                CommonClaims.USER_ID, 1)
        );

        final RequestContext requestContext = makeRequestContext(request, Map.of("degreeId", "5"));

        HttpResponse response = degreeController.getDegree(requestContext);

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        ArgumentCaptor<WritableBodyContent> responseBody = captureResponseBody(response);
        assertEquals(HttpStatusCode.FORBIDDEN, responseStatusCode.getValue());
        assertTrue(responseBody.getAllValues().isEmpty());
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
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, Map.of(
                CommonClaims.AUTHENTICATED, true,
                CommonClaims.USER_ID, 1)
        );

        HttpResponse response = degreeController.getDegrees(makeRequestContext(request));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());

        verify(degreeManager, times(1)).getDegreesForStudent(1);
    }

    @Test
    void testUnauthorizedCreateDegree() {
        HttpRequest request = makeHttpRequest(makeJsonRequestBody(Degree.class, new Degree()));
        AuthMockHelpers.configureFailingAuthHandler(authenticationHandler);

        HttpResponse response = degreeController.createDegree(makeRequestContext(request));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.UNAUTHORIZED, responseStatusCode.getValue());
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
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, Map.of(
                CommonClaims.AUTHENTICATED, true,
                CommonClaims.USER_ID, 1)
        );

        RequestContext context = makeRequestContext(request, Map.of("degreeId", "1"));

        HttpResponse response = degreeController.deleteDegree(context);

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.OK, responseStatusCode.getValue());

        verify(degreeManager, times(1)).deleteDegree(1);
    }

    @Test
    void testIllegalDelete() {
        Degree unownedDegree = new Degree();
        unownedDegree.setId(1);
        unownedDegree.setOwnerId(2);
        unownedDegree.setName("Test Degree");
        unownedDegree.setDescription("Test Description");

        when(degreeManager.getDegree(1)).thenReturn(unownedDegree);

        HttpRequest request = makeHttpRequest();

        RequestContext context = makeRequestContext(request, Map.of("degreeId", "1"));

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, Map.of(
                CommonClaims.AUTHENTICATED, true,
                CommonClaims.USER_ID, 1)
        );

        HttpResponse response = degreeController.deleteDegree(context);

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.FORBIDDEN, responseStatusCode.getValue());
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
