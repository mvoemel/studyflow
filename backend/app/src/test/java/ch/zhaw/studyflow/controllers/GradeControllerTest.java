package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.services.persistence.GradeDao;
import ch.zhaw.studyflow.webserver.http.CaptureContainer;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContent;
import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GradeControllerTest {

    @Mock
    private GradeDao gradeDao;
    @Mock
    private AuthenticationHandler authenticator;
    @InjectMocks
    private GradeController gradeController;
    @Mock
    private HttpRequest request;
    @Mock
    private HttpResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.createResponse()).thenReturn(response);
        when(response.setStatusCode(any())).thenReturn(response);
        when(response.setResponseBody(any())).thenReturn(response);
    }

    private void configureSuccessfulAuth() {
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticator, Map.of(
                CommonClaims.USER_ID, 1L,
                CommonClaims.AUTHENTICATED, true
        ));
    }

    private void configureFailingAuth() {
        AuthMockHelpers.configureFailingAuthHandler(authenticator);
    }

    private RequestContext mockRequestContext() {
        RequestContext context = mock(RequestContext.class);
        when(context.getRequest()).thenReturn(request);
        return context;
    }

    @Test
    void testGetGradesByDegreeIdSuccessful() throws NoSuchFieldException, IllegalAccessException {
        configureSuccessfulAuth();
        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));
        Grade grade = new Grade();
        List<Grade> grades = List.of(grade);
        when(gradeDao.readByDegree(1)).thenReturn(grades);

        HttpResponse actualResponse = gradeController.getGradesByDegreeId(context);

        verify(gradeDao).readByDegree(1);
        verify(response).setStatusCode(HttpStatusCode.OK);

        ArgumentCaptor<WritableBodyContent> argumentCaptor = ArgumentCaptor.forClass(WritableBodyContent.class);
        verify(response).setResponseBody(argumentCaptor.capture());
        WritableBodyContent capturedContent = argumentCaptor.getValue();
        assertTrue(capturedContent.getClass().getSimpleName().equals("WritableJsonContent"));

        var contentField = capturedContent.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        Object content = contentField.get(capturedContent);
        assertTrue(content instanceof Map);

        Map<Long, Map<Long, List<Grade>>> expected = Map.of(0L, Map.of(0L, grades));
        assertEquals(expected, content);
    }

    @Test
    void testPatchGradesByDegreeIdSuccessful() {
        configureSuccessfulAuth();
        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));
        Map<String, Object> moduleData = Map.of(
                "id", 1L,
                "grades", List.of(
                        Map.of("id", 1L, "name", "Test", "percentage", 1.0, "value", 100.0)
                )
        );
        ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(bodyContent.tryRead(Map.class)).thenReturn(Optional.of(moduleData));

        HttpResponse actualResponse = gradeController.patchGradesByDegreeId(context);

        verify(gradeDao).updateByDegree(eq(1L), anyList());
        verify(response).setStatusCode(HttpStatusCode.OK);
    }

    @Test
    void testGetGradesAveragesByDegreeIdSuccessful() throws NoSuchFieldException, IllegalAccessException {
        configureSuccessfulAuth();
        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));
        List<Grade> grades = List.of(new Grade());
        when(gradeDao.readByDegree(1)).thenReturn(grades);

        HttpResponse actualResponse = gradeController.getGradesAveragesByDegreeId(context);

        verify(gradeDao).readByDegree(1);
        verify(response).setStatusCode(HttpStatusCode.OK);

        ArgumentCaptor<WritableBodyContent> argumentCaptor = ArgumentCaptor.forClass(WritableBodyContent.class);
        verify(response).setResponseBody(argumentCaptor.capture());
        WritableBodyContent capturedContent = argumentCaptor.getValue();
        assertTrue(capturedContent.getClass().getSimpleName().equals("WritableJsonContent"));

        var contentField = capturedContent.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        Object content = contentField.get(capturedContent);
        assertTrue(content instanceof Map);
    }
}