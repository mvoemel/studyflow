package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.webserver.http.CaptureContainer;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContent;
import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CalendarControllerTest {
    private CalendarManager calendarManager;
    private AuthenticationHandler authenticator;
    private CalendarController calendarController;
    private AppointmentManager appointmentManager;

    @BeforeEach
    void setUp() {
        appointmentManager  = mock(AppointmentManager.class);
        calendarManager     = mock(CalendarManager.class);
        authenticator       = mock(AuthenticationHandler.class);

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
        when(calendarManager.read(anyLong(), anyLong())).thenReturn(calendar);

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticator, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));

        final HttpRequest request = HttpMockHelpers.makeHttpRequest();
        final RequestContext context = HttpMockHelpers.makeRequestContext(request, Map.of("id", "1"));

        final HttpResponse actualResponse = calendarController.getCalendar(context);

        final ArgumentCaptor<HttpStatusCode> statusCodeCapture = HttpMockHelpers.captureResponseCode(actualResponse);
        final ArgumentCaptor<WritableBodyContent> responseBodyCapture = HttpMockHelpers.captureResponseBody(actualResponse);

        assertInstanceOf(JsonContent.class, responseBodyCapture.getValue());
        assertEquals(HttpStatusCode.OK, statusCodeCapture.getValue());
    }

    @Test
    void testGetCalendars() {
        List<Calendar> calendars = List.of(new Calendar());

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticator, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));

        when(calendarManager.getCalendarsByUserId(1L)).thenReturn(calendars);

        final HttpRequest request = HttpMockHelpers.makeHttpRequest();
        final RequestContext context = HttpMockHelpers.makeRequestContext(request, Map.of());

        final HttpResponse actualResponse = calendarController.getCalendars(context);
        final ArgumentCaptor<WritableBodyContent> responseBodyCaptor = HttpMockHelpers.captureResponseBody(actualResponse);
        final ArgumentCaptor<HttpStatusCode> statusCodeCaptor = HttpMockHelpers.captureResponseCode(actualResponse);

        assertInstanceOf(JsonContent.class, responseBodyCaptor.getValue());
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

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticator, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));

        final HttpRequest request = HttpMockHelpers.makeHttpRequest();
        final RequestContext context = HttpMockHelpers.makeRequestContext(request, Map.of("calendarId", "1"));
        HttpMockHelpers.addQueryParameters(request, Map.of("from", List.of("2021-01-01"), "to", List.of("2021-01-02")));

        when(appointmentManager.readAllBy(anyLong(), any(), any())).thenReturn(appointments);

        HttpResponse actualResponse = calendarController.getAppointments(context);
        ArgumentCaptor<HttpStatusCode> statusCodeCaptor = HttpMockHelpers.captureResponseCode(actualResponse);
        assertEquals(HttpStatusCode.OK, statusCodeCaptor.getValue());
    }

    @Test
    void testGetAppointmentsByDate() {
        final String from = "2021-01-01";
        final String to = "2021-01-02";

        List<Appointment> appointments = List.of(new Appointment());
        when(appointmentManager.readAllBy(
                anyLong(),
                assertArg(e -> validateDate(LocalDate.parse(from), e)),
                assertArg(e -> validateDate(LocalDate.parse(to), e))))
                .thenReturn(appointments);

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticator, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));

        HttpRequest request = HttpMockHelpers.makeHttpRequest();
        RequestContext context = HttpMockHelpers.makeRequestContext(request, Map.of("calendarId", "1"));
        HttpMockHelpers.addQueryParameters(request, Map.of("from", List.of(from), "to", List.of(to)));

        final HttpResponse actualResponse = calendarController.getAppointmentsByDate(context);

        final ArgumentCaptor<WritableBodyContent> responseBodyCaptor = HttpMockHelpers.captureResponseBody(actualResponse);
        final ArgumentCaptor<HttpStatusCode> statusCodeCaptor = HttpMockHelpers.captureResponseCode(actualResponse);
        assertInstanceOf(JsonContent.class, responseBodyCaptor.getValue());
        assertEquals(HttpStatusCode.OK, statusCodeCaptor.getValue());
    }

    private static void validateDate(LocalDate expected, LocalDate actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + " but was " + actual);
        }
    }

    @Test
    void testGetAppointment() {
        Appointment appointment = new Appointment();

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticator, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));
        when(appointmentManager.read(anyLong(), anyLong())).thenReturn(appointment);

        final HttpRequest request = HttpMockHelpers.makeHttpRequest();
        final RequestContext context = HttpMockHelpers.makeRequestContext(
                request,
                Map.of(
                        "calendarId", "1",
                        "appointmentId", "1"
                )
        );

        final HttpResponse actualResponse = calendarController.getAppointment(context);
        final ArgumentCaptor<WritableBodyContent> responseBodyCaptor = HttpMockHelpers.captureResponseBody(actualResponse);
        final ArgumentCaptor<HttpStatusCode> statusCodeCaptor = HttpMockHelpers.captureResponseCode(actualResponse);
        assertInstanceOf(JsonContent.class, responseBodyCaptor.getValue());
        assertEquals(HttpStatusCode.OK, statusCodeCaptor.getValue());
    }

    @Test
    void testDeleteAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticator, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));

        final HttpRequest request     = HttpMockHelpers.makeHttpRequest();
        final RequestContext context  = HttpMockHelpers.makeRequestContext(request, Map.of(
                "calendarId", "1",
                "appointmentId", "1")
        );

        final HttpResponse actualResponse = calendarController.deleteAppointment(context);
        final ArgumentCaptor<HttpStatusCode> statusCodeCaptor = HttpMockHelpers.captureResponseCode(actualResponse);

        verify(appointmentManager, times(1)).delete(appointment.getId());
        assertEquals(HttpStatusCode.NO_CONTENT, statusCodeCaptor.getValue());
    }

    @Test
    void testDeleteCalendar() {
        Calendar calendar = new Calendar();
        calendar.setId(1L);

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticator, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));

        final ReadableBodyContent bodyContent = HttpMockHelpers.makeJsonRequestBody(Calendar.class, calendar);
        final HttpRequest request = HttpMockHelpers.makeHttpRequest(bodyContent);
        final RequestContext context = HttpMockHelpers.makeRequestContext(request, Map.of("id", "1"));

        final HttpResponse actualResponse = calendarController.deleteCalendar(context);

        final ArgumentCaptor<HttpStatusCode> statusCodeCaptor = HttpMockHelpers.captureResponseCode(actualResponse);

        verify(calendarManager, times(1)).delete(calendar.getId(), 1L);
        assertEquals(HttpStatusCode.NO_CONTENT, statusCodeCaptor.getValue());
    }
}