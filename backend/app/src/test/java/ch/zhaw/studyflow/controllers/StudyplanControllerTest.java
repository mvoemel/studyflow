package ch.zhaw.studyflow.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static ch.zhaw.studyflow.controllers.HttpMockHelpers.captureResponseCode;
import static ch.zhaw.studyflow.controllers.HttpMockHelpers.makeHttpRequest;
import static ch.zhaw.studyflow.controllers.HttpMockHelpers.makeJsonRequestBody;
import static ch.zhaw.studyflow.controllers.HttpMockHelpers.makeRequestContext;
import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.domain.studyplan.StudyplanManager;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;

public class StudyplanControllerTest {
    private AuthenticationHandler authenticationHandler;
    private SemesterManager semesterManager;
    private StudyplanManager studyplanManager;
    private StudyplanController studyplanController;


    @BeforeEach
    public void setUp() {
        authenticationHandler = mock(AuthenticationHandler.class);
        semesterManager = mock(SemesterManager.class);
        studyplanManager = mock(StudyplanManager.class);
        studyplanController = new StudyplanController(studyplanManager, authenticationHandler, semesterManager);      

    }

    private StudyplanParameters createTestStudyplanParameters() {
        StudyplanParameters parameters = new StudyplanParameters();
        parameters.setSettingsId(1L);
        parameters.setSemesterId(1L);
        parameters.setStartDate(LocalDate.of(2025,1,1));
        parameters.setEndDate(LocalDate.of(2025,1,31));
        parameters.setDayStartTime(LocalTime.of(8,0));
        parameters.setDayEndTime(LocalTime.of(18,0));
        parameters.setDaysOfWeek(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
        return parameters;
    }

        @Test
    public void testCreateStudyplan() {
        // Arrange
        StudyplanParameters parameters = createTestStudyplanParameters();
        long userId = 1L;
        long studyplanId = 123L;
        
        when(studyplanManager.createStudyplan(parameters, userId)).thenReturn(studyplanId);

        HttpRequest request = makeHttpRequest(makeJsonRequestBody(StudyplanParameters.class, parameters));
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        HttpResponse response = studyplanController.generateStudyplan(makeRequestContext(request));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.CREATED, responseStatusCode.getValue());

        verify(studyplanManager).createStudyplan(parameters, userId);
    }
    
       
}

