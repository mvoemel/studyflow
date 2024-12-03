package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.ModuleGrade;
import ch.zhaw.studyflow.controllers.deo.SemesterGrade;
import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.domain.grade.GradeManager;
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

class GradeControllerTest {
    @Mock
    private SemesterManager semesterManager;
    @Mock
    private GradeManager gradeManager;
    @Mock
    private ModuleManager moduleManager;
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
                CommonClaims.USER_ID, 1L
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

        Semester semester = new Semester();
        semester.setId(1L);
        semester.setName("Semester 1");
        List<Semester> semesters = List.of(semester);
        when(semesterManager.getSemestersForDegree(1L)).thenReturn(semesters);

        Module module = new Module(1L, "Module 1");
        List<Module> modules = List.of(module);
        when(moduleManager.getModulesBySemester(1L)).thenReturn(modules);

        Grade grade = new Grade();
        List<Grade> grades = List.of(grade);
        when(gradeManager.getGradesByModule(1L)).thenReturn(grades);

        HttpResponse actualResponse = gradeController.getGradesByDegreeId(context);

        verify(semesterManager).getSemestersForDegree(1L);
        verify(moduleManager).getModulesBySemester(1L);
        verify(gradeManager).getGradesByModule(1L);
        verify(response).setStatusCode(HttpStatusCode.OK);

        ArgumentCaptor<WritableBodyContent> argumentCaptor = ArgumentCaptor.forClass(WritableBodyContent.class);
        verify(response).setResponseBody(argumentCaptor.capture());
        WritableBodyContent capturedContent = argumentCaptor.getValue();
        assertTrue(capturedContent.getClass().getSimpleName().equals("WritableJsonContent"));

        var contentField = capturedContent.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        Object content = contentField.get(capturedContent);
        assertTrue(content instanceof List);

        List<SemesterGrade> expected = List.of(new SemesterGrade(1L, "Semester 1", List.of(new ModuleGrade(1L, "Module 1", grades))));
        assertSemesterGradesEqual(expected, (List<SemesterGrade>) content);
    }

    private void assertSemesterGradesEqual(List<SemesterGrade> expected, List<SemesterGrade> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            SemesterGrade expectedSemesterGrade = expected.get(i);
            SemesterGrade actualSemesterGrade = actual.get(i);
            assertEquals(expectedSemesterGrade.getId(), actualSemesterGrade.getId());
            assertEquals(expectedSemesterGrade.getName(), actualSemesterGrade.getName());
            assertModuleGradesEqual(expectedSemesterGrade.getModules(), actualSemesterGrade.getModules());
        }
    }

    private void assertModuleGradesEqual(List<ModuleGrade> expected, List<ModuleGrade> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            ModuleGrade expectedModuleGrade = expected.get(i);
            ModuleGrade actualModuleGrade = actual.get(i);
            assertEquals(expectedModuleGrade.getId(), actualModuleGrade.getId());
            assertEquals(expectedModuleGrade.getName(), actualModuleGrade.getName());
            assertEquals(expectedModuleGrade.getGrades(), actualModuleGrade.getGrades());
        }
    }

    @Test
    void testPatchGradesByDegreeIdSuccessful() {
        configureSuccessfulAuth();
        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));
        ModuleGrade moduleGrade = new ModuleGrade(1L, "Module", List.of(new Grade(1L, "Test", 1.0, 100.0, 1L)));
        ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(bodyContent.tryRead(ModuleGrade.class)).thenReturn(Optional.of(moduleGrade));

        HttpResponse actualResponse = gradeController.patchGradesByDegreeId(context);

        verify(gradeManager).updateGradesByModule(eq(1L), anyList());
        verify(response).setStatusCode(HttpStatusCode.OK);
    }

    @Test
    void testGetGradesAveragesByDegreeIdSuccessful() throws NoSuchFieldException, IllegalAccessException {
        configureSuccessfulAuth();
        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));
        List<Grade> grades = List.of(new Grade(1L, "Test", 1.0, 100.0, 1L));
        when(gradeManager.getGradesByModule(1L)).thenReturn(grades);

        HttpResponse actualResponse = gradeController.getGradesAveragesByDegreeId(context);

        verify(gradeManager).getGradesByModule(1);
        verify(response).setStatusCode(HttpStatusCode.OK);

        ArgumentCaptor<WritableBodyContent> argumentCaptor = ArgumentCaptor.forClass(WritableBodyContent.class);
        verify(response).setResponseBody(argumentCaptor.capture());
        WritableBodyContent capturedContent = argumentCaptor.getValue();
        assertTrue(capturedContent.getClass().getSimpleName().equals("WritableJsonContent"));

        var contentField = capturedContent.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        Object content = contentField.get(capturedContent);
        assertTrue(content instanceof Map);

        Map<String, Double> expected = Map.of("average", 100.0);
        assertEquals(expected, content);
    }

    @Test
    void testGetGradesByDegreeIdMissingDegreeId() {
        configureSuccessfulAuth();
        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.empty());

        HttpResponse actualResponse = gradeController.getGradesByDegreeId(context);

        verify(response).setStatusCode(HttpStatusCode.BAD_REQUEST);
    }

    @Test
    void testPatchGradesByDegreeIdInvalidBody() {
        configureSuccessfulAuth();
        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));
        when(request.getRequestBody()).thenReturn(Optional.empty());

        HttpResponse actualResponse = gradeController.patchGradesByDegreeId(context);

        verify(response).setStatusCode(HttpStatusCode.BAD_REQUEST);
    }

    @Test
    void testGetGradesByDegreeIdUnauthorized() {
        configureFailingAuth();
        RequestContext context = mockRequestContext();
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));

        HttpResponse actualResponse = gradeController.getGradesByDegreeId(context);

        verify(response).setStatusCode(HttpStatusCode.UNAUTHORIZED);
    }
}