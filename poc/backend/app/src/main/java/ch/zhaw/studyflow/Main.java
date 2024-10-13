package ch.zhaw.studyflow;

import ch.zhaw.studyflow.controllers.UserController;
import ch.zhaw.studyflow.webserver.WebServerBuilder;
import ch.zhaw.studyflow.webserver.sun.SunHttpServerWebServerBuilder;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        WebServerBuilder webServerBuilder = SunHttpServerWebServerBuilder.create(new InetSocketAddress(8080));
        webServerBuilder.configureControllers(controllerRegistry -> {
            controllerRegistry.registerController(UserController.class);
        });
        webServerBuilder.build().start();
    }
}