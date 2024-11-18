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
import ch.zhaw.studyflow.webserver.http.query.QueryParameters;
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
    private QueryParameters queryParameters;

    @BeforeEach
    void setUp() {
        appointmentManager = mock(AppointmentManager.class);
        calendarManager = mock(CalendarManager.class);
        authenticator = mock(AuthenticationHandler.class);
        principalProvider = mock(PrincipalProvider.class);
        context = mock(RequestContext.class);
        request = mock(HttpRequest.class);
        response = mock(HttpResponse.class);
        queryParameters = mock(QueryParameters.class);

        when(context.getRequest()).thenReturn(request);
        when(request.createResponse()).thenReturn(response);
        when(response.getResponseBody()).thenReturn(JsonContent.writableOf(""));
        when(response.setResponseBody(any())).thenReturn(response);
        when(request.getQueryParameters()).thenReturn(queryParameters);

        calendarController = new CalendarController(calendarManager, appointmentManager, authenticator);
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
        Principal principal = createAuthenticatedPrincipal();
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer(principal));
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(principalProvider.getPrincipal(any())).thenReturn(mock(Principal.class));
        when(calendarManager.read(anyLong(), anyLong())).thenReturn(calendar);

        ArgumentCaptor<HttpStatusCode> statusCodeCaptor = ArgumentCaptor.forClass(HttpStatusCode.class);

        HttpResponse actualResponse = calendarController.getCalendar(context);
        verify(response, times(1)).setStatusCode(statusCodeCaptor.capture());
        assertEquals(HttpStatusCode.OK, statusCodeCaptor.getValue());
    }

    @Test
    void testGetCalendars() {
        List<Calendar> calendars = List.of(new Calendar());

        Principal principal = createAuthenticatedPrincipal();
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer(principal));
        when(principalProvider.getPrincipal(any())).thenReturn(mock(Principal.class));
        when(calendarManager.getCalendarsByUserId(1L)).thenReturn(calendars);

        ArgumentCaptor<HttpStatusCode> statusCodeCaptor = ArgumentCaptor.forClass(HttpStatusCode.class);

        HttpResponse actualResponse = calendarController.getCalendars(context);
        verify(response, times(1)).setStatusCode(statusCodeCaptor.capture());
        assertEquals(HttpStatusCode.OK, statusCodeCaptor.getValue());
    }

    private Principal createAuthenticatedPrincipal() {
        Principal principal = mock(Principal.class);
        when(principal.getClaim(CommonClaims.USER_ID)).thenReturn(Optional.of(1));
        when(principal.getClaim(CommonClaims.AUTHENTICATED)).thenReturn(Optional.of(true));
        return principal;
    }

    @Test
    void testGetAppointments() {
        List<Appointment> appointments = List.of(new Appointment());

        Principal principal = createAuthenticatedPrincipal();
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer(principal));
        when(principalProvider.getPrincipal(any())).thenReturn(mock(Principal.class));
        when(appointmentManager.readAllBy(anyLong(), any(), any())).thenReturn(appointments);

        ArgumentCaptor<HttpStatusCode> statusCodeCaptor = ArgumentCaptor.forClass(HttpStatusCode.class);

        HttpResponse actualResponse = calendarController.getAppointments(context);
        verify(response, times(1)).setStatusCode(statusCodeCaptor.capture());
        assertEquals(HttpStatusCode.OK, statusCodeCaptor.getValue());
    }

    @Test
    void testGetAppointmentsByDate() {
        List<Appointment> appointments = List.of(new Appointment());

        Principal principal = createAuthenticatedPrincipal();
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer(principal));
        when(principalProvider.getPrincipal(any())).thenReturn(mock(Principal.class));
        when(appointmentManager.readAllBy(anyLong(), any(Date.class), any(Date.class))).thenReturn(appointments);

        ArgumentCaptor<HttpStatusCode> statusCodeCaptor = ArgumentCaptor.forClass(HttpStatusCode.class);

        HttpResponse actualResponse = calendarController.getAppointmentsByDate(context);
        verify(response, times(1)).setStatusCode(statusCodeCaptor.capture());
        assertEquals(HttpStatusCode.OK, statusCodeCaptor.getValue());
    }

    @Test
    void testGetAppointment() {
        Appointment appointment = new Appointment();
        Principal principal = createAuthenticatedPrincipal();
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer(principal));
        when(principalProvider.getPrincipal(any())).thenReturn(principal);
        when(appointmentManager.read(anyLong(), anyLong())).thenReturn(appointment);

        QueryParameters queryParameters = mock(QueryParameters.class);
        when(queryParameters.getSingleValue("calendarId")).thenReturn(Optional.of("1"));
        when(queryParameters.getSingleValue("appointmentId")).thenReturn(Optional.of("1"));
        when(request.getQueryParameters()).thenReturn(queryParameters);

        ArgumentCaptor<HttpStatusCode> statusCodeCaptor = ArgumentCaptor.forClass(HttpStatusCode.class);

        HttpResponse actualResponse = calendarController.getAppointment(context);
        verify(response, times(1)).setStatusCode(statusCodeCaptor.capture());
        assertEquals(HttpStatusCode.OK, statusCodeCaptor.getValue());
    }

    @Test
    void testDeleteAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        Principal principal = createAuthenticatedPrincipal();
        when(principal.getClaim(any())).thenReturn(Optional.of(1));

        when(bodyContent.tryRead(Appointment.class)).thenReturn(Optional.of(appointment));
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer(principal));
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(principalProvider.getPrincipal(any())).thenReturn(principal);

        ArgumentCaptor<HttpStatusCode> statusCodeCaptor = ArgumentCaptor.forClass(HttpStatusCode.class);

        HttpResponse actualResponse = calendarController.deleteAppointment(context);
        verify(response, times(1)).setStatusCode(statusCodeCaptor.capture());
        verify(appointmentManager, times(1)).delete(appointment.getId());
        assertEquals(HttpStatusCode.NO_CONTENT, statusCodeCaptor.getValue());
    }

    @Test
    void testDeleteCalendar() {
        Calendar calendar = new Calendar();
        calendar.setId(1L);
        ReadableBodyContent bodyContent = mock(ReadableBodyContent.class);
        Principal principal = createAuthenticatedPrincipal();
        when(principal.getClaim(any())).thenReturn(Optional.of(1));

        when(bodyContent.tryRead(Calendar.class)).thenReturn(Optional.of(calendar));
        when(authenticator.handleIfAuthenticated(any(), any(Function.class))).thenAnswer(authenticatedActionAnswer(principal));
        when(request.getRequestBody()).thenReturn(Optional.of(bodyContent));
        when(principalProvider.getPrincipal(any())).thenReturn(principal);

        ArgumentCaptor<HttpStatusCode> statusCodeCaptor = ArgumentCaptor.forClass(HttpStatusCode.class);

        HttpResponse actualResponse = calendarController.deleteCalendar(context);
        verify(response, times(1)).setStatusCode(statusCodeCaptor.capture());
        verify(calendarManager, times(1)).delete(calendar.getId(), 1L);
        assertEquals(HttpStatusCode.NO_CONTENT, statusCodeCaptor.getValue());
    }
}