package ch.zhaw.studyflow;

import ch.zhaw.studyflow.controllers.CalendarController;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.domain.calendar.impls.AppointmentManagerImpl;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.services.persistence.AppointmentDao;
import ch.zhaw.studyflow.services.persistence.CalendarDao;
import ch.zhaw.studyflow.domain.calendar.impls.CalendarManagerImpl;
import ch.zhaw.studyflow.controllers.StudentController;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.domain.student.impls.StudentManagerImpl;
import ch.zhaw.studyflow.services.persistence.memory.InMemoryAppointmentDao;
import ch.zhaw.studyflow.services.persistence.memory.InMemoryCalendarDao;
import ch.zhaw.studyflow.services.persistence.memory.InMemorySettingsDao;
import ch.zhaw.studyflow.services.persistence.memory.InMemoryStudentDao;
import ch.zhaw.studyflow.services.persistence.SettingsDao;
import ch.zhaw.studyflow.services.persistence.StudentDao;
import ch.zhaw.studyflow.webserver.WebServerBuilder;
import ch.zhaw.studyflow.webserver.http.contents.*;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.authentication.JwtBasedAuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;
import ch.zhaw.studyflow.webserver.security.principal.jwt.JwtHashAlgorithm;
import ch.zhaw.studyflow.webserver.security.principal.jwt.JwtPrincipalProvider;
import ch.zhaw.studyflow.webserver.security.principal.jwt.JwtPrincipalProviderOptions;
import ch.zhaw.studyflow.webserver.sun.SunHttpServerWebServerBuilder;

import java.net.InetSocketAddress;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;

public class Main {
    public static void main(String[] args) {
        loadLoggerConfig();
        final Map<String, ReadableBodyContentInstanceFactory> contentTypes = Map.of(
                JsonContent.MIME_TYPE_JSON, JsonContent::readableOf,
                TextContent.MIME_TEXT_PLAIN, TextContent::readableOf
        );

        WebServerBuilder webServerBuilder = SunHttpServerWebServerBuilder.create(new InetSocketAddress(8080));
        webServerBuilder.configureControllers(controllerRegistry -> {
            controllerRegistry.register(
                    CalendarController.class,
                    serviceCollection -> new CalendarController(
                            serviceCollection.getRequiredService(AuthenticationHandler.class),
                            serviceCollection.getRequiredService(CalendarManagerImpl.class),
                            serviceCollection.getRequiredService(AppointmentManagerImpl.class)
                    ));
            controllerRegistry.register(
                    StudentController.class,
                    serviceCollection -> new StudentController(
                            serviceCollection.getRequiredService(AuthenticationHandler.class),
                            serviceCollection.getRequiredService(PrincipalProvider.class),
                            serviceCollection.getRequiredService(StudentManager.class)
                    ));
        });
        webServerBuilder.configureServices(builder -> {
            // REGISTER DAO'S
            builder.registerSingelton(CalendarDao.class, serviceCollection -> new InMemoryCalendarDao());
            builder.registerSingelton(AppointmentDao.class, serviceCollection -> new InMemoryAppointmentDao());
            builder.registerSingelton(StudentDao.class, serviceCollection -> new InMemoryStudentDao());
            builder.registerSingelton(SettingsDao.class, serviceCollection -> new InMemorySettingsDao());

            // REGISTER MANAGERS
            builder.register(CalendarManager.class, serviceCollection -> new CalendarManagerImpl(
                    serviceCollection.getRequiredService(CalendarDao.class)
            ));
            builder.register(AppointmentManager.class, serviceCollection -> new AppointmentManagerImpl(
                    serviceCollection.getRequiredService(AppointmentDao.class)
            ));
            builder.register(StudentManager.class, serviceCollection -> new StudentManagerImpl(
                    serviceCollection.getRequiredService(CalendarManager.class),
                    serviceCollection.getRequiredService(StudentDao.class),
                    serviceCollection.getRequiredService(SettingsDao.class)
            ));

            // REGISTER AUTHENTICATION SERVICES
            builder.registerSingelton(PrincipalProvider.class, serviceCollection -> new JwtPrincipalProvider(
                    new JwtPrincipalProviderOptions("secret", JwtHashAlgorithm.HS256, "superdupersecret", Duration.ofDays(1)),
                    List.of(CommonClaims.EXPIRES, CommonClaims.USER_ID)
            ));
            builder.registerSingelton(AuthenticationHandler.class, serviceCollection ->
                    new JwtBasedAuthenticationHandler(
                            serviceCollection.getRequiredService(PrincipalProvider.class),
                            Duration.ofHours(2)
                    )
            );

            // REGISTER BODY CONTENT FACTORY
            builder.registerSingelton(
                    ReadableBodyContentFactory.class,
                    serviceCollection -> new MapReadableBodyContentFactory(serviceCollection, contentTypes)
            );
        });
        webServerBuilder.build().start();
    }

    private static void loadLoggerConfig() {
        try {
            LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}