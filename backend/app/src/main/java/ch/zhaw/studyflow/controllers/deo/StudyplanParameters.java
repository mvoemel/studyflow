package ch.zhaw.studyflow.controllers.deo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonGetter;

public class StudyplanParameters {
    private long settingsId;
    private long semesterId;
    private LocalDate startDate;
    


    @JsonGetter("semesterId")
    public long getSemesterId() {
        return this.semesterId;
    }
}