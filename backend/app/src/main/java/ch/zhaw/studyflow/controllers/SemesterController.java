package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.webserver.annotations.Route;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;

@Route(path="semesters")
public class SemesterController {

    private final AuthenticationHandler authenticationHandler;
    private final SemesterManager semesterManager;

    public SemesterController(AuthenticationHandler authenticationHandler, SemesterManager semesterManager) {
        this.authenticationHandler = authenticationHandler;
        this.semesterManager = semesterManager;
    }



}
