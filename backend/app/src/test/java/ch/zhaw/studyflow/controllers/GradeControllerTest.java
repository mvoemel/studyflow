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
    void testCreateGradeSuccessful() {
        configureSuccessfulAuth();
        Grade grade = new Grade();
        ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(bodyContent.tryRead(Grade.class)).thenReturn(Optional.of(grade));
        doNothing().when(gradeDao).create(grade);

        HttpResponse actualResponse = gradeController.createGrade(mockRequestContext());

        verify(gradeDao).create(grade);
        verify(response).setStatusCode(HttpStatusCode.CREATED);
    }

    @Test
    void testCreateGradeUnauthorized() {
        configureFailingAuth();

        HttpResponse actualResponse = gradeController.createGrade(mockRequestContext());

        verify(response).setStatusCode(HttpStatusCode.UNAUTHORIZED);
    }

    @Test
    void testUpdateGradeSuccessful() {
        configureSuccessfulAuth();
        Grade grade = new Grade();
        grade.setId(1);
        ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(bodyContent.tryRead(Grade.class)).thenReturn(Optional.of(grade));
        doNothing().when(gradeDao).update(grade);

        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("gradeId")).thenReturn(Optional.of("1"));

        HttpResponse actualResponse = gradeController.updateGrade(context);

        verify(gradeDao).update(grade);
        verify(response).setStatusCode(HttpStatusCode.OK);
    }

    @Test
    void testDeleteGradeSuccessful() {
        configureSuccessfulAuth();
        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("gradeId")).thenReturn(Optional.of("1"));
        doNothing().when(gradeDao).delete(1);

        HttpResponse actualResponse = gradeController.deleteGrade(context);

        verify(gradeDao).delete(1);
        verify(response).setStatusCode(HttpStatusCode.OK);
    }

    @Test
    void testGetGradesByModIdSuccessful() throws NoSuchFieldException, IllegalAccessException {
        configureSuccessfulAuth();
        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("modId")).thenReturn(Optional.of("1"));
        List<Grade> grades = List.of(new Grade());
        when(gradeDao.readByModule(1)).thenReturn(grades);

        HttpResponse actualResponse = gradeController.getGradesByModId(context);

        verify(gradeDao).readByModule(1);
        verify(response).setStatusCode(HttpStatusCode.OK);

        ArgumentCaptor<WritableBodyContent> argumentCaptor = ArgumentCaptor.forClass(WritableBodyContent.class);
        verify(response).setResponseBody(argumentCaptor.capture());
        WritableBodyContent capturedContent = argumentCaptor.getValue();
        assertTrue(capturedContent.getClass().getSimpleName().equals("WritableJsonContent"));

        var contentField = capturedContent.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        Object content = contentField.get(capturedContent);
        assertEquals(grades, content);
    }
}