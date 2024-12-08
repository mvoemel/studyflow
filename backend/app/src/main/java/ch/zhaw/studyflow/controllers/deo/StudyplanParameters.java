package ch.zhaw.studyflow.controllers.deo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

public class StudyplanParameters {
    private long settingsId;
    private long semesterId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DayOfWeek> daysOfWeek;
    private LocalTime dayStartTime;
    private LocalTime dayEndTime;
    

    public StudyplanParameters() {
    }

    public StudyplanParameters(long settingsId, long semesterId, LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek, LocalTime dayStartTime, LocalTime dayEndTime) {
        this.settingsId = settingsId;
        this.semesterId = semesterId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.daysOfWeek = daysOfWeek;
        this.dayStartTime = dayStartTime;
        this.dayEndTime = dayEndTime;
    }

    @JsonGetter("settingsId")
    public long getSettingsId() {
        return this.settingsId;
    }

    @JsonGetter("semesterId")
    public long getSemesterId() {
        return this.semesterId;
    }

    @JsonGetter("startDate")
    public LocalDate getStartDate() {
        return this.startDate;
    }

    @JsonGetter("endDate")
    public LocalDate getEndDate() {
        return this.endDate;
    }

    @JsonGetter("daysOfWeek")
    public List<DayOfWeek> getDaysOfWeek() {
        return this.daysOfWeek;
    }

    @JsonGetter("dayStartTime")
    public LocalTime getDayStartTime() {
        return this.dayStartTime;
    }

    @JsonGetter("dayEndTime")
    public LocalTime getDayEndTime() {
        return this.dayEndTime;
    }


}