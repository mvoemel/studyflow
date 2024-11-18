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
import ch.zhaw.studyflow.webserver.http.query.QueryParameters;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Controller for handling calendar-related HTTP requests.
 * Provides endpoints for creating, reading, updating, and deleting calendars and appointments.
 */
@Route(path = "api/calendar")
public class CalendarController {
    private static final Logger logger = Logger.getLogger(CalendarController.class.getName());
    private final CalendarManager calendarManager;
    private final AppointmentManager appointmentManager;
    private final AuthenticationHandler authenticator;

    /**
     * Constructs a CalendarController with the specified managers and authenticator.
     *
     * @param calendarManager the manager for calendar operations
     * @param appointmentManager the manager for appointment operations
     * @param authenticator the handler for authentication
     */
    public CalendarController(CalendarManager calendarManager, AppointmentManager appointmentManager, AuthenticationHandler authenticator) {
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
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Calendar.class))
                    .flatMap(calendar -> {
                        calendarManager.create(calendar);
                        return Optional.of(calendar);
                    })
                    .ifPresentOrElse(
                            calendar -> {
                                response.setResponseBody(JsonContent.writableOf(calendar))
                                        .setStatusCode(HttpStatusCode.CREATED);
                            },
                            () -> {
                                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                            }
                    );
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
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Appointment.class))
                    .flatMap(appointment -> {
                        appointmentManager.create(appointment);
                        return Optional.of(appointment);
                    })
                    .ifPresentOrElse(
                            appointment -> {
                                response.setResponseBody(JsonContent.writableOf(appointment))
                                        .setStatusCode(HttpStatusCode.CREATED);
                            },
                            () -> {
                                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                            }
                    );
            return response;
        });
    }

    /**
     * Endpoint for retrieving a specific calendar.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = ":id")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getCalendar(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Calendar.class))
                    .flatMap(calendar -> {
                        Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
                        if (userId.isPresent()) {
                            Calendar foundCalendar = calendarManager.read(calendar.getId(), userId.get());
                            return Optional.of(foundCalendar);
                        } else {
                            return Optional.empty();
                        }
                    })
                    .ifPresentOrElse(
                            calendar -> {
                                response.setResponseBody(JsonContent.writableOf(calendar));
                                response.setStatusCode(HttpStatusCode.OK);
                            },
                            () -> {
                                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                            }
                    );
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
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                List<Calendar> calendars = calendarManager.getCalendarsByUserId(userId.get());
                response.setResponseBody(JsonContent.writableOf(calendars))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
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
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                QueryParameters queryParameters = request.getQueryParameters();
                Date from = queryParameters.getSingleValue("from")
                        .map(Date::new)
                        .orElse(new Date(0));
                Date to = queryParameters.getSingleValue("to")
                        .map(Date::new)
                        .orElse(new Date());
                List<Appointment> appointments = appointmentManager.readAllBy(userId.get(), from, to);
                response.setResponseBody(JsonContent.writableOf(appointments))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    /**
     * Endpoint for retrieving a specific appointment by date.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = "{calendarId}/appointments/{appointmentId}")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointmentsByDate(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse(); // Simplified method call

        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                QueryParameters queryParameters = request.getQueryParameters();

                Date from = queryParameters.getSingleValue("from")
                        .map(Date::new)
                        .orElseGet(() -> {
                            java.util.Calendar calendar = java.util.Calendar.getInstance();
                            calendar.add(java.util.Calendar.MONTH, -1);
                            return calendar.getTime();
                        });

                Date to = queryParameters.getSingleValue("to")
                        .map(Date::new)
                        .orElseGet(Date::new);

                List<Appointment> appointments = appointmentManager.readAllBy(userId.get(), from, to);
                response.setResponseBody(JsonContent.writableOf(appointments))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
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
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                QueryParameters queryParameters = request.getQueryParameters();
                Optional<Long> calendarId = queryParameters.getSingleValue("calendarId").map(Long::valueOf);
                Optional<Long> appointmentId = queryParameters.getSingleValue("appointmentId").map(Long::valueOf);
                if (calendarId.isPresent() && appointmentId.isPresent()) {
                    Appointment foundAppointment = appointmentManager.read(calendarId.get(), appointmentId.get());
                    response.setResponseBody(JsonContent.writableOf(foundAppointment))
                            .setStatusCode(HttpStatusCode.OK);
                } else {
                    response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                }
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
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
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Appointment.class))
                    .ifPresentOrElse(
                            appointment -> {
                                appointmentManager.delete(appointment.getId());
                                response.setStatusCode(HttpStatusCode.NO_CONTENT);
                            },
                            () -> {
                                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                            }
                    );
            return response;
        });
    }

    /**
     * Endpoint for deleting a specific calendar.
     *
     * @param context the request context
     * @return the HTTP response
     */
    @Route(path = ":id")
    @Endpoint(method = HttpMethod.DELETE)
    public HttpResponse deleteCalendar(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = request.createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            request.getRequestBody()
                    .flatMap(body -> body.tryRead(Calendar.class))
                    .ifPresentOrElse(
                            calendar -> {
                                Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
                                if (userId.isPresent()) {
                                    calendarManager.delete(calendar.getId(), userId.get());
                                    response.setStatusCode(HttpStatusCode.NO_CONTENT);
                                } else {
                                    response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                                }
                            },
                            () -> {
                                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                            }
                    );
            return response;
        });
    }
}