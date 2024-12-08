package ch.zhaw.studyflow;

import ch.zhaw.studyflow.controllers.*;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.domain.calendar.impls.AppointmentManagerImpl;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.domain.curriculum.DegreeManager;
import ch.zhaw.studyflow.domain.curriculum.impls.DegreeManagerImpl;
import ch.zhaw.studyflow.domain.curriculum.impls.ModuleManagerImpl;
import ch.zhaw.studyflow.domain.curriculum.impls.SemesterManagerImpl;
import ch.zhaw.studyflow.domain.grade.GradeManager;
import ch.zhaw.studyflow.domain.grade.impls.GradeManagerImpl;
import ch.zhaw.studyflow.services.persistence.*;
import ch.zhaw.studyflow.domain.calendar.impls.CalendarManagerImpl;
import ch.zhaw.studyflow.services.persistence.memory.*;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.domain.student.impls.StudentManagerImpl;
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
import java.time.Duration;
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
                            serviceCollection.getRequiredService(CalendarManager.class),
                            serviceCollection.getRequiredService(AppointmentManager.class)
                    ));
            controllerRegistry.register(
                    ModuleController.class,
                    serviceCollection -> new ModuleController(
                            serviceCollection.getRequiredService(ModuleManager.class),
                            serviceCollection.getRequiredService(AuthenticationHandler.class)

                    ));
            controllerRegistry.register(
                    StudentController.class,
                    serviceCollection -> new StudentController(
                            serviceCollection.getRequiredService(AuthenticationHandler.class),
                            serviceCollection.getRequiredService(PrincipalProvider.class),
                            serviceCollection.getRequiredService(StudentManager.class)
                    ));
            controllerRegistry.register(
                    SemesterController.class,
                    serviceCollection -> new SemesterController(
                            serviceCollection.getRequiredService(AuthenticationHandler.class),
                            serviceCollection.getRequiredService(SemesterManager.class)
                    ));
            controllerRegistry.register(
                    DegreeController.class,
                    serviceCollection -> new DegreeController(
                            serviceCollection.getRequiredService(AuthenticationHandler.class),
                            serviceCollection.getRequiredService(DegreeManager.class)
                    )
            );
            controllerRegistry.register(
                    GradeController.class,
                    serviceCollection -> new GradeController(
                            serviceCollection.getRequiredService(SemesterManager.class),
                            serviceCollection.getRequiredService(ModuleManager.class),
                            serviceCollection.getRequiredService(GradeManager.class),
                            serviceCollection.getRequiredService(AuthenticationHandler.class)
                    )
            );
        });
        webServerBuilder.configureServices(builder -> {
            // REGISTER DAO'S
            builder.registerSingelton(CalendarDao.class, serviceCollection -> new InMemoryCalendarDao());
            builder.registerSingelton(AppointmentDao.class, serviceCollection -> new InMemoryAppointmentDao());
            builder.registerSingelton(StudentDao.class, serviceCollection -> new InMemoryStudentDao());
            builder.registerSingelton(SettingsDao.class, serviceCollection -> new InMemorySettingsDao());
            builder.registerSingelton(SemesterDao.class, serviceCollection -> new InMemorySemesterDao());
            builder.registerSingelton(ModuleDao.class, serviceCollection -> new InMemoryModuleDao());
            builder.registerSingelton(DegreeDao.class, serviceCollection -> new InMemoryDegreeDao());
            builder.registerSingelton(GradeDao.class, serviceCollection -> new InMemoryGradeDao());

            // REGISTER MANAGERS
            builder.register(CalendarManager.class, serviceCollection -> new CalendarManagerImpl(
                    serviceCollection.getRequiredService(CalendarDao.class)
            ));

            builder.registerSingelton(AppointmentDao.class, serviceCollection -> new InMemoryAppointmentDao());
            builder.register(AppointmentManager.class, serviceCollection -> new AppointmentManagerImpl(
                    serviceCollection.getRequiredService(AppointmentDao.class)
            ));
            builder.register(StudentManager.class, serviceCollection -> new StudentManagerImpl(
                    serviceCollection.getRequiredService(CalendarManager.class),
                    serviceCollection.getRequiredService(StudentDao.class),
                    serviceCollection.getRequiredService(SettingsDao.class)
            ));
            builder.register(ModuleManager.class, serviceCollection -> new ModuleManagerImpl(
                    serviceCollection.getRequiredService(ModuleDao.class)
            ));

            builder.register(DegreeManager.class, serviceCollection -> new DegreeManagerImpl(
                    serviceCollection.getRequiredService(StudentManager.class),
                    serviceCollection.getRequiredService(DegreeDao.class),
                    serviceCollection.getRequiredService(SemesterDao.class)
            ));

            builder.register(SemesterManager.class, serviceCollection -> new SemesterManagerImpl(
                    serviceCollection.getRequiredService(DegreeManager.class),
                    serviceCollection.getRequiredService(SemesterDao.class),
                    serviceCollection.getRequiredService(ModuleDao.class)
            ));

            builder.register(GradeManager.class, serviceCollection -> new GradeManagerImpl(
                    serviceCollection.getRequiredService(GradeDao.class)
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