package ch.zhaw.studyflow.domain.student;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Settings {
    private long settingsId;
    private long globalCalendarId;
    private long activeDegree;
    private long activeSemester;

    @JsonGetter("id")
    public long getId() {
        return settingsId;
    }

    @JsonSetter("id")
    public void setId(long settingsId) {
        this.settingsId = settingsId;
    }

    @JsonGetter("globalCalendar")
    public long getGlobalCalendarId() {
        return globalCalendarId;
    }

    @JsonSetter("globalCalendar")
    public void setGlobalCalendarId(long globalCalendarId) {
        this.globalCalendarId = globalCalendarId;
    }

    @JsonGetter("activeDegree")
    public long getActiveDegree() {
        return activeDegree;
    }

    @JsonSetter("activeDegree")
    public void setActiveDegree(long activeDegree) {
        this.activeDegree = activeDegree;
    }

    @JsonGetter("activeSemester")
    public long getActiveSemester() {
        return activeSemester;
    }

    @JsonSetter("activeSemester")
    public void setActiveSemester(long activeSemester) {
        this.activeSemester = activeSemester;
    }
}