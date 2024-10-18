package ch.zhaw.studyflow.webserver.controllers;

import java.util.*;
import java.util.logging.Logger;


public class SimpleControllerRegistry implements ControllerRegistry {
    private static final Logger logger = Logger.getLogger(SimpleControllerRegistry.class.getName());

    private final List<ControllerMetadata> registeredControllers;


    public SimpleControllerRegistry(List<ControllerMetadata> controllerMetadata) {
        this.registeredControllers  = Collections.unmodifiableList(new ArrayList<>(controllerMetadata));
    }

    @Override
    public List<ControllerMetadata> getRegisteredControllers() {
        return registeredControllers;
    }
}
