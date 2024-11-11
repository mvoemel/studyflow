package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.webserver.annotations.Endpoint;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Route(path = "api/calendar")
public class CalendarController {
    private static final Logger logger = Logger.getLogger(CalendarController.class.getName());
    private final CalendarManager calendarManager;
    private final AppointmentManager appointmentManager;
    private final AuthenticationHandler authenticator;
    private final PrincipalProvider principalProvider;

    public CalendarController(CalendarManager calendarManager, AppointmentManager appointmentManager, AuthenticationHandler authenticator, PrincipalProvider principalProvider) {
        this.calendarManager = calendarManager;
        this.appointmentManager = appointmentManager;
        this.authenticator = authenticator;
        this.principalProvider = principalProvider;
    }

    @Endpoint(method = HttpMethod.POST)
    public HttpResponse createCalendar(RequestContext context, long userId, Calendar calendar) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            calendarManager.create(calendar);
            HttpResponse response = context.getRequest().createResponse();
            return response.setResponseBody(JsonContent.writableOf("Calendar created"));
        });
    }

    @Route(path = "createAppointment")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse createAppointment(RequestContext context, long userId, long calendarId) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            Appointment appointment = new Appointment();
            appointmentManager.create(appointment);
            HttpResponse response = context.getRequest().createResponse();
            return response.setResponseBody(JsonContent.writableOf("Appointment created"));
        });
    }

    @Route(path = "getCalendar")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getCalendar(RequestContext context, long userId, long calendarId) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            Calendar calendar = calendarManager.read(userId, calendarId);
            HttpResponse response = context.getRequest().createResponse();
            return response.setResponseBody(JsonContent.writableOf(calendar));
        });
    }

    @Route(path = "getCalendars")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getCalendars(RequestContext context) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            List<Calendar> calendars = calendarManager.getCalendars();
            HttpResponse response = context.getRequest().createResponse();
            return response.setResponseBody(JsonContent.writableOf(calendars));
        });
    }

    @Route(path = "getAppointments")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointments(RequestContext context, long userId, long calendarId) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            List<Appointment> appointments = appointmentManager.readAllBy(calendarId, null, null);
            HttpResponse response = context.getRequest().createResponse();
            return response.setResponseBody(JsonContent.writableOf(appointments));
        });
    }

    @Route(path = "getAppointmentsByDate")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointments(RequestContext context, long userId, long calendarId, Date from, Date to) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            List<Appointment> appointments = appointmentManager.readAllBy(calendarId, from, to);
            HttpResponse response = context.getRequest().createResponse();
            return response.setResponseBody(JsonContent.writableOf(appointments));
        });
    }

    @Route(path = "getAppointment")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointment(RequestContext context, long userId, long calendarId, long appointmentId) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            Appointment appointment = appointmentManager.read(calendarId, appointmentId);
            HttpResponse response = context.getRequest().createResponse();
            return response.setResponseBody(JsonContent.writableOf(appointment));
        });
    }

    @Route(path = "deleteAppointment")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteAppointment(RequestContext context, long calendarId, long appointmentId) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            appointmentManager.delete(appointmentId);
            HttpResponse response = context.getRequest().createResponse();
            return response.setStatusCode(HttpStatusCode.NO_CONTENT);
        });
    }

    @Route(path = "deleteCalendar")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteCalendar(RequestContext context, long userId, long id) {
        return authenticator.handleIfAuthenticated(context.getRequest(), principal -> {
            calendarManager.delete(userId, id);
            HttpResponse response = context.getRequest().createResponse();
            return response.setStatusCode(HttpStatusCode.NO_CONTENT);
        });
    }
}