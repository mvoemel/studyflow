package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CalendarControllerTest {

    private CalendarManager calendarManager;
    private AuthenticationHandler authenticator;
    private PrincipalProvider principalProvider;
    private CalendarController calendarController;
    private RequestContext context;
    private HttpRequest request;
    private HttpResponse response;
    private AppointmentManager appointmentManager;

    @BeforeEach
    void setUp() {
        appointmentManager = mock(AppointmentManager.class);
        calendarManager = mock(CalendarManager.class);
        authenticator = mock(AuthenticationHandler.class);
        principalProvider = mock(PrincipalProvider.class);
        context = mock(RequestContext.class);
        request = mock(HttpRequest.class);
        response = mock(HttpResponse.class);

        when(context.getRequest()).thenReturn(request);
        when(request.createResponse()).thenReturn(response);
        when(response.getResponseBody()).thenReturn(JsonContent.writableOf(""));
        when(response.setResponseBody(any())).thenReturn(response);

        calendarController = new CalendarController(calendarManager, appointmentManager, authenticator, principalProvider);
    }

    private Answer<HttpResponse> authenticatedActionAnswer() {
        return authenticatedActionAnswer(mock(Principal.class));
    }

    private Answer<HttpResponse> authenticatedActionAnswer(Principal principal) {
        return invocation -> {
            Function<Principal, HttpResponse> action = invocation.getArgument(1);
            return action.apply(principal);
        };
    }

    @Test
    void testGetCalendar() {
        Calendar calendar = new Calendar();
        ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        when(bodyContent.tryRead(Calendar.class)).thenReturn(Optional.of(calendar));
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(principalProvider.getPrincipal(any())).thenReturn(mock(Principal.class));
        when(calendarManager.read(anyLong(), anyLong())).thenReturn(calendar);

        HttpResponse actualResponse = calendarController.getCalendar(context);

        assertEquals(HttpStatusCode.OK, actualResponse.getStatusCode());
    }

    @Test
    void testGetCalendars() {
        List<Calendar> calendars = List.of(new Calendar());
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(principalProvider.getPrincipal(any())).thenReturn(mock(Principal.class));
        when(calendarManager.getCalendars()).thenReturn(calendars);

        ArgumentCaptor<HttpStatusCode> statusCodeCaptor = ArgumentCaptor.forClass(HttpStatusCode.class);

        HttpResponse actualResponse = calendarController.getCalendars(context);
        verify(response, times(1)).setStatusCode(statusCodeCaptor.capture());
        assertEquals(HttpStatusCode.OK, statusCodeCaptor.getValue());
    }

    @Test
    void testGetAppointments() {
        List<Appointment> appointments = List.of(new Appointment());
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(principalProvider.getPrincipal(any())).thenReturn(mock(Principal.class));
        when(appointmentManager.readAllBy(anyLong(), any(), any())).thenReturn(appointments);

        HttpResponse actualResponse = calendarController.getAppointments(context);

        assertEquals(HttpStatusCode.OK, actualResponse.getStatusCode());
    }

    @Test
    void testGetAppointmentsByDate() {
        List<Appointment> appointments = List.of(new Appointment());
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(principalProvider.getPrincipal(any())).thenReturn(mock(Principal.class));
        when(appointmentManager.readAllBy(anyLong(), any(Date.class), any(Date.class))).thenReturn(appointments);

        HttpResponse actualResponse = calendarController.getAppointmentsByDate(context, new Date(), new Date());

        assertEquals(HttpStatusCode.OK, actualResponse.getStatusCode());
    }

    @Test
    void testGetAppointment() {
        Appointment appointment = new Appointment();
        ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        when(bodyContent.tryRead(Appointment.class)).thenReturn(Optional.of(appointment));
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(principalProvider.getPrincipal(any())).thenReturn(mock(Principal.class));
        when(appointmentManager.read(anyLong(), anyLong())).thenReturn(appointment);

        HttpResponse actualResponse = calendarController.getAppointment(context);

        assertEquals(HttpStatusCode.OK, actualResponse.getStatusCode());
    }

    @Test
    void testDeleteAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        Principal principal = mock(Principal.class);
        when(principal.getClaim(any())).thenReturn(Optional.of(1));

        when(bodyContent.tryRead(Appointment.class)).thenReturn(Optional.of(appointment));
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer(principal));
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(principalProvider.getPrincipal(any())).thenReturn(principal);


        HttpResponse actualResponse = calendarController.deleteAppointment(context);

        verify(appointmentManager, times(1)).delete(appointment.getId());
        assertEquals(HttpStatusCode.OK, actualResponse.getStatusCode());
    }

    @Test
    void testDeleteCalendar() {
        Calendar calendar = new Calendar();
        ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        when(bodyContent.tryRead(Calendar.class)).thenReturn(Optional.of(calendar));
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer());
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(principalProvider.getPrincipal(any())).thenReturn(mock(Principal.class));
        when(principalProvider.getPrincipal(any()).getClaim(any())).thenReturn(Optional.of("1"));

        HttpResponse actualResponse = calendarController.deleteCalendar(context);

        verify(calendarManager, times(1)).delete(anyLong(), anyLong());
        assertEquals(HttpStatusCode.NO_CONTENT, actualResponse.getStatusCode());
    }
}