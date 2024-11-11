package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.TextContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CalendarControllerTest {

    private CalendarManager calendarManager;
    private AppointmentManager appointmentManager;
    private AuthenticationHandler authenticator;
    private PrincipalProvider principalProvider;
    private CalendarController calendarController;
    private RequestContext context;
    private HttpRequest request;
    private HttpResponse response;

    @BeforeEach
    void setUp() {
        calendarManager = mock(CalendarManager.class);
        appointmentManager = mock(AppointmentManager.class);
        authenticator = mock(AuthenticationHandler.class);
        principalProvider = mock(PrincipalProvider.class);
        context = mock(RequestContext.class);
        request = mock(HttpRequest.class);
        response = mock(HttpResponse.class);

        when(context.getRequest()).thenReturn(request);
        when(request.createResponse()).thenReturn(response);
        when(response.getResponseBody()).thenReturn(TextContent.writableOf(""));
        when(response.getStatusCode()).thenReturn(HttpStatusCode.OK);

        calendarController = new CalendarController(calendarManager, appointmentManager, authenticator, principalProvider);
    }

    private Answer<HttpResponse> authenticatedActionAnswer() {
        return invocation -> {
            Function<RequestContext, HttpResponse> action = invocation.getArgument(1);
            RequestContext context = invocation.getArgument(0);
            return action.apply(context);
        };
    }

    @Test
    void testCreateCalendar() {
        Calendar calendar = new Calendar();
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(response.getResponseBody()).thenReturn(TextContent.writableOf("Calendar created"));

        HttpResponse actualResponse = calendarController.createCalendar(context, 1L, calendar);

        verify(calendarManager, times(1)).create(calendar);
        assertEquals("Calendar created", actualResponse.getResponseBody().toString());
    }

    @Test
    void testCreateAppointment() {
        Appointment appointment = new Appointment();
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(response.getResponseBody()).thenReturn(TextContent.writableOf("Appointment created"));

        HttpResponse actualResponse = calendarController.createAppointment(context, 1L, 1L);

        verify(appointmentManager, times(1)).create(any(Appointment.class));
        assertEquals("Appointment created", actualResponse.getResponseBody().toString());
    }

    @Test
    void testGetCalendar() {
        Calendar calendar = new Calendar();
        when(calendarManager.read(anyLong(), anyLong())).thenReturn(calendar);
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(response.getResponseBody()).thenReturn(TextContent.writableOf(calendar.toString()));

        HttpResponse actualResponse = calendarController.getCalendar(context, 1L, 1L);

        verify(calendarManager, times(1)).read(1L, 1L);
        assertEquals(calendar.toString(), actualResponse.getResponseBody().toString());
    }

    @Test
    void testDeleteAppointment() {
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(response.getStatusCode()).thenReturn(HttpStatusCode.NO_CONTENT);

        HttpResponse actualResponse = calendarController.deleteAppointment(context, 1L, 1L);

        verify(appointmentManager, times(1)).delete(1L);
        assertEquals(HttpStatusCode.NO_CONTENT, actualResponse.getStatusCode());
    }

    @Test
    void testDeleteCalendar() {
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(response.getStatusCode()).thenReturn(HttpStatusCode.NO_CONTENT);

        HttpResponse actualResponse = calendarController.deleteCalendar(context, 1L, 1L);

        verify(calendarManager, times(1)).delete(1L, 1L);
        assertEquals(HttpStatusCode.NO_CONTENT, actualResponse.getStatusCode());
    }
}