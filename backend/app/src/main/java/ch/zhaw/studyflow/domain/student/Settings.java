package ch.zhaw.studyflow.domain.student;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * The domain model of the students settings.
 */
public class Settings {
    private long id;
    private long globalCalendarId;
    private long activeDegree;

    public Settings() {
        this.id                 = -1;
        this.globalCalendarId   = -1;
        this.activeDegree       = -1;
    }

    @JsonGetter("id")
    public long getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
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
}
