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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetGradesByDegreeIdSuccessful() throws NoSuchFieldException, IllegalAccessException {
        configureSuccessfulAuth();
        RequestContext context = HttpMockHelpers.makeRequestContext(HttpMockHelpers.makeHttpRequest());
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));

        final List<Semester> semesters = List.of(
                new Semester(1L, "Semester 1", "", 0L, 0)
        );
        when(semesterManager.getSemestersForDegree(1L)).thenReturn(semesters);

        final List<Module> modules = List.of(
                new Module(1L, "Module 1")
        );
        when(moduleManager.getModulesBySemester(1L)).thenReturn(modules);

        final List<Grade> grades = List.of(
                new Grade(0L, "Test 1", 0.5, 100, 1L),
                new Grade(1L, "Test 2", 0.5, 100, 1L)
        );
        when(gradeManager.getGradesByModule(1L)).thenReturn(grades);

        final HttpResponse actualResponse = gradeController.getGradesByDegreeId(context);

        verify(semesterManager).getSemestersForDegree(1L);
        verify(moduleManager).getModulesBySemester(1L);
        verify(gradeManager).getGradesByModule(1L);
        verify(actualResponse).setStatusCode(HttpStatusCode.OK);

        ArgumentCaptor<WritableBodyContent> bodyContentArgumentCaptor = ArgumentCaptor.forClass(WritableBodyContent.class);
        verify(actualResponse).setResponseBody(bodyContentArgumentCaptor.capture());

        WritableBodyContent capturedContent = bodyContentArgumentCaptor.getValue();
        assertEquals("WritableJsonContent", capturedContent.getClass().getSimpleName());
        List<?> content = getContentField(capturedContent, List.class);

        final List<SemesterGrade> expected = List.of(
                new SemesterGrade(1L, "Semester 1", List.of(new ModuleGrade(1L, "Module 1", grades)))
        );
        //noinspection unchecked
        assertSemesterGradesEqual(expected, (List<SemesterGrade>)content);
    }

    @Test
    void testPatchGradesByDegreeIdSuccessful() {
        configureSuccessfulAuth();

        final RequestContext context = HttpMockHelpers.makeRequestContext(HttpMockHelpers.makeHttpRequest());
        final CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));
        final ModuleGrade moduleGrade = new ModuleGrade(1L, "Module", List.of(new Grade(1L, "Test", 1.0, 100.0, 1L)));

        final ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        when(bodyContent.tryRead(ModuleGrade.class)).thenReturn(Optional.of(moduleGrade));

        final HttpResponse actualResponse = gradeController.patchGradesByDegreeId(context);

        verify(gradeManager).updateGradesByModule(eq(1L), anyList());
        verify(actualResponse).setStatusCode(HttpStatusCode.OK);
    }

    @Test
    void testGetGradesAveragesByDegreeIdSuccessful() throws NoSuchFieldException, IllegalAccessException {
        configureSuccessfulAuth();

        final RequestContext context = HttpMockHelpers.makeRequestContext(HttpMockHelpers.makeHttpRequest());
        final CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));

        final List<Grade> grades = List.of(new Grade(1L, "Test", 1.0, 100.0, 1L));
        when(gradeManager.getGradesByModule(1L)).thenReturn(grades);

        final HttpResponse response = gradeController.getGradesAveragesByDegreeId(context);

        verify(gradeManager).getGradesByModule(1);
        verify(response).setStatusCode(HttpStatusCode.OK);

        final ArgumentCaptor<WritableBodyContent> bodyContentArgumentCaptor = ArgumentCaptor.forClass(WritableBodyContent.class);
        verify(response).setResponseBody(bodyContentArgumentCaptor.capture());
        final WritableBodyContent capturedContent = bodyContentArgumentCaptor.getValue();
        assertEquals("WritableJsonContent", capturedContent.getClass().getSimpleName());

        final Map<?, ?> content = getContentField(capturedContent, Map.class);
        assertInstanceOf(Map.class, content);

        final Map<String, Double> expected = Map.of("average", 100.0);
        assertEquals(expected, content);
    }

    @Test
    void testGetGradesByDegreeIdMissingDegreeId() {
        configureSuccessfulAuth();
        RequestContext context = HttpMockHelpers.makeRequestContext(HttpMockHelpers.makeHttpRequest());
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);

        final HttpResponse response = gradeController.getGradesByDegreeId(context);

        verify(response).setStatusCode(HttpStatusCode.BAD_REQUEST);
    }

    @Test
    void testPatchGradesByDegreeIdInvalidBody() {
        configureSuccessfulAuth();
        RequestContext context = HttpMockHelpers.makeRequestContext(HttpMockHelpers.makeHttpRequest());
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));

        final HttpResponse response = gradeController.patchGradesByDegreeId(context);

        verify(response).setStatusCode(HttpStatusCode.BAD_REQUEST);
    }


    @Test
    void testGetGradesByDegreeIdUnauthorized() {
        configureFailingAuth();
        RequestContext context = HttpMockHelpers.makeRequestContext(HttpMockHelpers.makeHttpRequest());
        CaptureContainer captureContainer = mock(CaptureContainer.class);
        when(context.getUrlCaptures()).thenReturn(captureContainer);
        when(captureContainer.get("degreeId")).thenReturn(Optional.of("1"));

        final HttpResponse response = gradeController.getGradesByDegreeId(context);

        verify(response).setStatusCode(HttpStatusCode.UNAUTHORIZED);
    }

    private static <T> T getContentField(WritableBodyContent content, Class<T> type) throws NoSuchFieldException, IllegalAccessException {
        var contentField = content.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        return type.cast(contentField.get(content));
    }

    private void configureSuccessfulAuth() {
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticator, Map.of(
                CommonClaims.USER_ID, 1L
        ));
    }

    private void configureFailingAuth() {
        AuthMockHelpers.configureFailingAuthHandler(authenticator);
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
}