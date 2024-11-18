package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.domain.curriculum.DegreeManager;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

import static ch.zhaw.studyflow.controllers.HttpMockHelpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
