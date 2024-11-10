package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.webserver.annotations.Endpoint;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.TextContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

/**
 * Controller class for handling calendar-related operations.
 * This class provides endpoints for creating, reading, updating, and deleting calendars and appointments.
 */
@Route(path = "calendar")
public class CalendarController {
    private final CalendarManager calendarManager;
    private final AppointmentManager appointmentManager;
    private final AuthenticationHandler authenticator;
    private final PrincipalProvider principalProvider;

    /**
     * Constructs a new CalendarController with the specified managers and handlers.
     *
     * @param calendarManager the calendar manager
     * @param appointmentManager the appointment manager
     * @param authenticator the authentication handler
     * @param principalProvider the principal provider
     */
    public CalendarController(CalendarManager calendarManager, AppointmentManager appointmentManager, AuthenticationHandler authenticator, PrincipalProvider principalProvider) {
        this.calendarManager = calendarManager;
        this.appointmentManager = appointmentManager;
        this.authenticator = authenticator;
        this.principalProvider = principalProvider;
    }

    /**
     * Endpoint for creating a new calendar.
     *
     * @param context the request context
     * @param userId the user ID
     * @param calendar the calendar to create
     * @return the HTTP response
     */
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse createCalendar(RequestContext context, long userId, Calendar calendar) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            calendarManager.create(calendar);
            return context.getRequest().createResponse().setResponseBody(TextContent.writableOf("Calendar created"));
        });
    }

    /**
     * Endpoint for creating a new appointment.
     *
     * @param context the request context
     * @param userId the user ID
     * @param calendarId the calendar ID
     * @return the HTTP response
     */
    @Route(path = "createAppointment")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse createAppointment(RequestContext context, long userId, long calendarId) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            Appointment appointment = new Appointment();
            appointmentManager.create(appointment);
            return context.getRequest().createResponse().setResponseBody(TextContent.writableOf("Appointment created"));
        });
    }

    /**
     * Endpoint for retrieving a calendar.
     *
     * @param context the request context
     * @param userId the user ID
     * @param calendarId the calendar ID
     * @return the HTTP response
     */
    @Route(path = "getCalendar")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getCalendar(RequestContext context, long userId, long calendarId) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            Calendar calendar = calendarManager.read(userId, calendarId);
            return context.getRequest().createResponse().setResponseBody(TextContent.writableOf(calendar.toString()));
        });
    }

    /**
     * Endpoint for retrieving all calendars.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "getCalendars")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getCalendars(RequestContext context) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            List<Calendar> calendars = calendarManager.getCalendars();
            return context.getRequest().createResponse().setResponseBody(TextContent.writableOf(calendars.toString()));
        });
    }

    /**
     * Endpoint for retrieving all appointments in a calendar.
     *
     * @param context the request context
     * @param userId the user ID
     * @param calendarId the calendar ID
     * @return the HTTP response
     */
    @Route(path = "getAppointments")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointments(RequestContext context, long userId, long calendarId) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            List<Appointment> appointments = appointmentManager.readAllBy(calendarId, null, null);
            return context.getRequest().createResponse().setResponseBody(TextContent.writableOf(appointments.toString()));
        });
    }

    /**
     * Endpoint for retrieving appointments in a calendar within a date range.
     *
     * @param context the request context
     * @param userId the user ID
     * @param calendarId the calendar ID
     * @param from the start date
     * @param to the end date
     * @return the HTTP response
     */
    @Route(path = "getAppointmentsByDate")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointments(RequestContext context, long userId, long calendarId, Date from, Date to) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            List<Appointment> appointments = appointmentManager.readAllBy(calendarId, from, to);
            return context.getRequest().createResponse().setResponseBody(TextContent.writableOf(appointments.toString()));
        });
    }

    /**
     * Endpoint for retrieving a specific appointment.
     *
     * @param context the request context
     * @param userId the user ID
     * @param calendarId the calendar ID
     * @param appointmentId the appointment ID
     * @return the HTTP response
     */
    @Route(path = "getAppointment")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointment(RequestContext context, long userId, long calendarId, long appointmentId) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            Appointment appointment = appointmentManager.read(calendarId, appointmentId);
            return context.getRequest().createResponse().setResponseBody(TextContent.writableOf(appointment.toString()));
        });
    }

    /**
     * Endpoint for deleting an appointment.
     *
     * @param context the request context
     * @param calendarId the calendar ID
     * @param appointmentId the appointment ID
     * @return the HTTP response
     */
    @Route(path = "deleteAppointment")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteAppointment(RequestContext context, long calendarId, long appointmentId) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            appointmentManager.delete(appointmentId);
            return context.getRequest().createResponse().setStatusCode(HttpStatusCode.NO_CONTENT);
        });
    }

    /**
     * Endpoint for deleting a calendar.
     *
     * @param context the request context
     * @param userId the user ID
     * @param id the calendar ID
     * @return the HTTP response
     */
    @Route(path = "deleteCalendar")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteCalendar(RequestContext context, long userId, long id) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            calendarManager.delete(userId, id);
            return context.getRequest().createResponse().setStatusCode(HttpStatusCode.NO_CONTENT);
        });
    }
}