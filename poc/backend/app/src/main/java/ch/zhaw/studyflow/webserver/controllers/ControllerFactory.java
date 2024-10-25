package ch.zhaw.studyflow.webserver.controllers;

import ch.zhaw.studyflow.services.ServiceCollection;

import java.util.function.Function;

@FunctionalInterface
public interface ControllerFactory<T> {
    T create(ServiceCollection serviceCollection);
}
