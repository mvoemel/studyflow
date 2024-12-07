package ch.zhaw.studyflow.controllers.deo;

import com.fasterxml.jackson.annotation.JsonGetter;

public class StudyplanParameters {
    private long settingsId;
    private long semesterId;


    @JsonGetter("semesterId")
    public long getSemesterId() {
        return this.semesterId;
    }
}