package ch.zhaw.studyflow.domain.student;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Settings {
    private long settingsId;
    private long globalCalendarId;


    public Settings() {
    }


    @JsonGetter
    public long getId() {
        return settingsId;
    }

    @JsonSetter
    public void setId(long settingsId) {
        this.settingsId = settingsId;
    }

    @JsonGetter
    public long getGlobalCalendarId() {
        return globalCalendarId;
    }

    @JsonSetter
    public void setGlobalCalendarId(long globalCalendarId) {
        this.globalCalendarId = globalCalendarId;
    }
}
