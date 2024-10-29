package ch.zhaw.studyflow.webserver.controllers;

import java.util.*;


public class SimpleControllerRegistry implements ControllerRegistry {
    private final List<ControllerMetadata<?>> registeredControllers;


    public SimpleControllerRegistry(List<ControllerMetadata<?>> controllerMetadata) {
        this.registeredControllers  = List.copyOf(controllerMetadata);
    }

    @Override
    public List<ControllerMetadata<?>> getRegisteredControllers() {
        return registeredControllers;
    }
}
