package ch.zhaw.studyflow;

import ch.zhaw.studyflow.controllers.UserController;
import ch.zhaw.studyflow.webserver.WebServerBuilder;
import ch.zhaw.studyflow.webserver.sun.SunHttpServerWebServerBuilder;

import java.net.InetSocketAddress;
import java.util.logging.LogManager;

public class Main {
    public static void main(String[] args) {
        loadLoggerConfig();
        WebServerBuilder webServerBuilder = SunHttpServerWebServerBuilder.create(new InetSocketAddress(8080));
        webServerBuilder.configureControllers(controllerRegistry -> {
            controllerRegistry.register(UserController.class, UserController::new);
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