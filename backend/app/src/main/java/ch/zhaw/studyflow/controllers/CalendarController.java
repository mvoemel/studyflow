package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.utils.LongUtils;
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

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling calendar-related HTTP requests.
 * Provides endpoints for creating, reading, updating, and deleting calendars and appointments.
 */
@Route(path = "api/calendars")
public class CalendarController {
    private final CalendarManager calendarManager;
    private final AppointmentManager appointmentManager;
    private final AuthenticationHandler authenticator;

    /**
     * Constructs a CalendarController with the specified managers and authenticator.
     *
     * @param calendarManager    the manager for calendar operations
     * @param appointmentManager the manager for appointment operations
     * @param authenticator      the handler for authentication
     */
    public CalendarController(AuthenticationHandler authenticator, CalendarManager calendarManager, AppointmentManager appointmentManager) {
        this.calendarManager = calendarManager;
        this.appointmentManager = appointmentManager;
        this.authenticator = authenticator;
    }

    /**
     * Endpoint for creating a new calendar.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse createCalendar(RequestContext context) {
        final HttpRequest request = context.getRequest();
        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = context.getRequest().createResponse()
                            .setStatusCode(HttpStatusCode.BAD_REQUEST);

            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Calendar.class))
                    .flatMap(calendar -> {
                        calendarManager.create(calendar);
                        return Optional.of(calendar);
                    })
                    .ifPresent(calendar -> {
                        response.setResponseBody(JsonContent.writableOf(calendar))
                                .setStatusCode(HttpStatusCode.CREATED);
                    });
            return response;
        });
    }

    /**
     * Endpoint for creating a new appointment in a specific calendar.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{calendarId}/appointments")
    @Endpoint(method = HttpMethod.POST)
    public HttpResponse createAppointment(RequestContext context) {
        final HttpRequest request = context.getRequest();
        return authenticator.handleIfAuthenticated(request, principal -> {
            HttpResponse response = context.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            final Optional<Long> calendarId = context.getUrlCaptures().get("calendarId").flatMap(LongUtils::tryParseLong);

            calendarId.flatMap(routeCalendarId ->
                    request.getRequestBody()
                            .flatMap(body -> body.tryRead(Appointment.class))
                            .flatMap(appointment -> {
                                if (appointment.getCalendarId() == routeCalendarId) {
                                    appointmentManager.create(appointment);
                                    return Optional.of(appointment);
                                }
                                return Optional.empty();
                            })
            ).ifPresent(appointment -> {
                response.setResponseBody(JsonContent.writableOf(appointment))
                        .setStatusCode(HttpStatusCode.CREATED);
            });
            return response;
        });
    }

    /**
     * Endpoint for retrieving a specific calendar.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{id}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getCalendar(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = context.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            final Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            final Optional<Long> calendarId = context.getUrlCaptures().get("id").flatMap(LongUtils::tryParseLong);
            if (userId.isPresent() && calendarId.isPresent()) {
                Calendar foundCalendar = calendarManager.read(calendarId.get(), userId.get());
                if (foundCalendar != null) {
                    response.setResponseBody(JsonContent.writableOf(foundCalendar))
                            .setStatusCode(HttpStatusCode.OK);
                }
            }
            return response;
        });
    }

    /**
     * Endpoint for retrieving all calendars for the authenticated user.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getCalendars(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = context.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            final Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            if (userId.isPresent()) {
                List<Calendar> calendars = calendarManager.getCalendarsByUserId(userId.get());
                response.setResponseBody(JsonContent.writableOf(calendars))
                        .setStatusCode(HttpStatusCode.OK);
            }
            return response;
        });
    }

    /**
     * Endpoint for retrieving all appointments in a specific calendar within a date range.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{calendarId}/appointments")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointments(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            HttpResponse response = request.createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            final Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            final Optional<Long> calendarId = context.getUrlCaptures().get("calendarId").flatMap(LongUtils::tryParseLong);
            if (userId.isPresent() && calendarId.isPresent()) {
                    final List<Appointment> appointments = appointmentManager.readAllBy(
                            calendarId.get()
                    );
                    response.setResponseBody(JsonContent.writableOf(appointments))
                            .setStatusCode(HttpStatusCode.OK);
            }
            return response;
        });
    }

    /**
     * Endpoint for retrieving a specific appointment.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{calendarId}/appointments/{appointmentId}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointment(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = context.getRequest().createResponse()
                    .setStatusCode(HttpStatusCode.BAD_REQUEST);

            final Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
            if (userId.isPresent()) {
                final Optional<Long> calendarId = context.getUrlCaptures().get("calendarId").flatMap(LongUtils::tryParseLong);
                final Optional<Long> appointmentId = context.getUrlCaptures().get("appointmentId").flatMap(LongUtils::tryParseLong);
                if (calendarId.isPresent() && appointmentId.isPresent()) {
                    Appointment foundAppointment = appointmentManager.read(calendarId.get(), appointmentId.get());
                    response.setResponseBody(JsonContent.writableOf(foundAppointment))
                            .setStatusCode(HttpStatusCode.OK);
                }
            }
            return response;
        });
    }

    /**
     * Endpoint for deleting a specific appointment.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{calendarId}/appointments/{appointmentId}")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteAppointment(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                            .setStatusCode(HttpStatusCode.BAD_REQUEST);

            Optional<Long> calendarId = context.getUrlCaptures().get("calendarId").flatMap(LongUtils::tryParseLong);
            Optional<Long> appointmentId = context.getUrlCaptures().get("appointmentId").flatMap(LongUtils::tryParseLong);
            if (calendarId.isPresent() && appointmentId.isPresent()) {
                appointmentManager.delete(appointmentId.get());
                response.setStatusCode(HttpStatusCode.NO_CONTENT);
            }
            return response;
        });
    }

    /**
     * Endpoint for deleting a specific calendar.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{id}")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteCalendar(RequestContext context) {
        final HttpRequest request = context.getRequest();

        return authenticator.handleIfAuthenticated(request, principal -> {
            final HttpResponse response = request.createResponse()
                            .setStatusCode(HttpStatusCode.BAD_REQUEST);

            Optional<Long> id = context.getUrlCaptures().get("id").flatMap(LongUtils::tryParseLong);
            if (id.isPresent()) {
                Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID);
                if (userId.isPresent()) {
                    calendarManager.delete(userId.get(), id.get());
                    response.setStatusCode(HttpStatusCode.NO_CONTENT);
                }
            }
            return response;
        });
    }
}