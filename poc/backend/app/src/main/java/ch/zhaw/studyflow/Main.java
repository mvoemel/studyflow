package ch.zhaw.studyflow;

import ch.zhaw.studyflow.controllers.UserController;
import ch.zhaw.studyflow.webserver.WebServerBuilder;
import ch.zhaw.studyflow.webserver.sun.SunHttpServerWebServerBuilder;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.getGlobal().setLevel(Level.ALL);
        WebServerBuilder webServerBuilder = SunHttpServerWebServerBuilder.create(new InetSocketAddress(8080));
        webServerBuilder.configureControllers(controllerRegistry -> {
            controllerRegistry.register(UserController.class, UserController::new);
        });
        webServerBuilder.build().start();
    }
}