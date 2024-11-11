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

    @Route(path = "createCalendar")
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

    @Route(path = "createAppointment")
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

    @Route(path = "getCalendar")
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

    @Route(path = "getCalendars")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getCalendars(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                List<Calendar> calendars = calendarManager.getCalendars();
                response.setResponseBody(JsonContent.writableOf(calendars))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    @Route(path = "getAppointments")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointments(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                List<Appointment> appointments = appointmentManager.readAllBy(userId.get(), null, null);
                response.setResponseBody(JsonContent.writableOf(appointments))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    @Route(path = "getAppointmentsByDate")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointmentsByDate(RequestContext context, Date from, Date to) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                List<Appointment> appointments = appointmentManager.readAllBy(userId.get(), from, to);
                response.setResponseBody(JsonContent.writableOf(appointments))
                        .setStatusCode(HttpStatusCode.OK);
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }

    @Route(path = "getAppointment")
    @Endpoint(method = HttpMethod.GET)
    public HttpResponse getAppointment(RequestContext context) {
        final HttpRequest request = context.getRequest();
        HttpResponse response = context.getRequest().createResponse();
        return authenticator.handleIfAuthenticated(request, principal -> {
            Optional<Long> userId = principal.getClaim(CommonClaims.USER_ID).map(Long::valueOf);
            if (userId.isPresent()) {
                request.getRequestBody()
                        .flatMap(body -> body.tryRead(Appointment.class))
                        .flatMap(appointment -> {
                            Appointment foundAppointment = appointmentManager.read(appointment.getCalendarId(), appointment.getId());
                            return Optional.of(foundAppointment);
                        })
                        .ifPresentOrElse(
                                appointment -> {
                                    response.setResponseBody(JsonContent.writableOf(appointment))
                                            .setStatusCode(HttpStatusCode.OK);
                                },
                                () -> {
                                    response.setStatusCode(HttpStatusCode.BAD_REQUEST);
                                }
                        );
            } else {
                response.setStatusCode(HttpStatusCode.BAD_REQUEST);
            }
            return response;
        });
    }


    @Route(path = "deleteAppointment")
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


    @Route(path = "deleteCalendar")
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