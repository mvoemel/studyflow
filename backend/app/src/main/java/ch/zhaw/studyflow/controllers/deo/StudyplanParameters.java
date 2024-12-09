package ch.zhaw.studyflow.controllers.deo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents the parameters for a study plan.
 */
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

    @JsonSetter("settingsId")
    public void setSettingsId(long settingsId) {
        this.settingsId = settingsId;
    }

    @JsonGetter("semesterId")
    public long getSemesterId() {
        return this.semesterId;
    }

    @JsonSetter("semesterId")
    public void setSemesterId(long semesterId) {
        this.semesterId = semesterId;
    }

    @JsonGetter("startDate")
    public LocalDate getStartDate() {
        return this.startDate;
    }

    @JsonSetter("startDate")
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @JsonGetter("endDate")
    public LocalDate getEndDate() {
        return this.endDate;
    }

    @JsonSetter("endDate")
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @JsonGetter("daysOfWeek")
    public List<DayOfWeek> getDaysOfWeek() {
        return this.daysOfWeek;
    }

    @JsonSetter("daysOfWeek")
    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    @JsonGetter("dayStartTime")
    public LocalTime getDayStartTime() {
        return this.dayStartTime;
    }

    @JsonSetter("dayStartTime")
    public void setDayStartTime(LocalTime dayStartTime) {
        this.dayStartTime = dayStartTime;
    }

    @JsonGetter("dayEndTime")
    public LocalTime getDayEndTime() {
        return this.dayEndTime;
    }

    @JsonSetter("dayEndTime")
    public void setDayEndTime(LocalTime dayEndTime) {
        this.dayEndTime = dayEndTime;
    }


}